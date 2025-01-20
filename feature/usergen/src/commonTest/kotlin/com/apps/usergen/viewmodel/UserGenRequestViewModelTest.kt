package com.apps.usergen.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.apps.usergen.data.UserCollection
import com.apps.usergen.repository.IdGenerator
import com.apps.usergen.repository.UserCollectionRepository
import com.apps.usergen.viewmodel.UserGenRequestViewModel.Companion.COUNT_KEY
import com.apps.usergen.viewmodel.UserGenRequestViewModel.Companion.NAME_KEY
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserGenRequestViewModelTest {

    private val mockUserCollectionRepository = mock<UserCollectionRepository>(MockMode.autoUnit)
    private val mockIdGenerator = mock<IdGenerator> {
        every { generateId() } returns GEN_ID
    }

    private lateinit var viewModel: UserGenRequestViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = UserGenRequestViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = mockUserCollectionRepository,
            idGenerator = mockIdGenerator,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun given_ValidCountString_then_CountValidation_isValid() = runTest {
        val count = 10


        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())

            viewModel.setCount(count.toString())

            assertEquals(UserGenUiState(
                count = count.toString(),
                validatedCount = UserGenUiState.ValidatedCount.Valid(count),
            ), awaitItem())
        }
    }

    @Test
    fun given_InvalidCountString_then_CountValidation_isInvalid() = runTest {
        val count = "absada"

        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())

            viewModel.setCount(count)

            assertEquals(UserGenUiState(
                count = count,
                validatedCount = UserGenUiState.ValidatedCount.Invalid,
            ), awaitItem())
        }
    }

    @Test
    fun given_ZeroCountString_then_CountValidation_isZero() = runTest {
        val count = 0

        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())

            viewModel.setCount(count.toString())

            assertEquals(UserGenUiState(
                count = count.toString(),
                validatedCount = UserGenUiState.ValidatedCount.Zero,
            ), awaitItem())
        }
    }

    @Test
    fun given_countClearedToEmptyString_then_CountValidation_isEmpty() = runTest {
        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())

            viewModel.setCount("10")

            assertEquals(UserGenUiState(
                count = "10",
                validatedCount = UserGenUiState.ValidatedCount.Valid(10),
            ), awaitItem())

            viewModel.setCount("")

            assertEquals(UserGenUiState(
                count = "",
                validatedCount = UserGenUiState.ValidatedCount.Empty,
            ), awaitItem())
        }
    }

    @Test
    fun given_restoredWithAvailableValue_then_uiStateIsCorrect() = runTest {
        val count = 10
        val collectionName = "test"
        viewModel = UserGenRequestViewModel(
            savedStateHandle = SavedStateHandle(mapOf(
                COUNT_KEY to count.toString(),
                NAME_KEY to collectionName,
            )),
            repository = mockUserCollectionRepository,
            idGenerator = mockIdGenerator,
        )

        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())
            assertEquals(UserGenUiState(
                name = collectionName,
                count = count.toString(),
                validatedCount = UserGenUiState.ValidatedCount.Valid(count),
            ), awaitItem())
        }
    }

    @Test
    fun given_userTypeCollectionName_then_uiState_shows_CollectionName() = runTest {
        val collectionName = "test"

        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())

            viewModel.setName(collectionName)

            assertEquals(UserGenUiState(
                name = collectionName,
            ), awaitItem())
        }
    }

    @Test
    fun given_GenerateUser_with_CorrectDetails_Then_Generate_UserParams() = runTest {
        val count = 10
        val collectionName = "test"

        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())

            viewModel.onGenerateUser(
                validatedCount = UserGenUiState.ValidatedCount.Valid(count),
                name = collectionName,
            )

            assertEquals(UserGenUiState(
                generateUserParams = GenUserParams(
                    id = GEN_ID,
                    name = collectionName,
                ),
            ), awaitItem())

            viewModel.consumeGenerateUserParams()

            assertEquals(UserGenUiState(
                generateUserParams = null,
            ), awaitItem())
        }

        verifySuspend(VerifyMode.exactly(1)) {
            mockIdGenerator.generateId()
            mockUserCollectionRepository.addUserCollection(
                UserCollection(
                    id = GEN_ID,
                    count = count,
                    name = collectionName,
                )
            )
        }
    }

    @Test
    fun given_GenerateUser_with_InvalidDetails_Then_No_UserParams() = runTest {
        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())

            viewModel.onGenerateUser(
                validatedCount = UserGenUiState.ValidatedCount.Invalid,
                name = "",
            )

            expectNoEvents()
        }

        verifySuspend(VerifyMode.not) {
            mockUserCollectionRepository.addUserCollection(any())
        }
    }

    @Test
    fun given_GenerateUser_GenerateIdThrownError_Then_No_UserParams() = runTest {
        val count = 10
        val collectionName = "test"

        every { mockIdGenerator.generateId() } throws Exception()

        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())

            viewModel.onGenerateUser(
                validatedCount = UserGenUiState.ValidatedCount.Valid(count),
                name = collectionName,
            )

            expectNoEvents()
        }

        verifySuspend(VerifyMode.not) {
            mockUserCollectionRepository.addUserCollection(any())
        }
    }

    @Test
    fun given_GenerateUser_AddCollectionThrownError_Then_No_UserParams() = runTest {
        val count = 10
        val collectionName = "test"

        everySuspend { mockUserCollectionRepository.addUserCollection(any()) } throws Exception()

        viewModel.uiState.test {
            assertEquals(UserGenUiState(), awaitItem())

            viewModel.onGenerateUser(
                validatedCount = UserGenUiState.ValidatedCount.Valid(count),
                name = collectionName,
            )

            expectNoEvents()
        }

        verifySuspend(VerifyMode.not) {
            mockUserCollectionRepository.addUserCollection(any())
        }
    }

    companion object {
        private const val GEN_ID = "testId"
    }
}