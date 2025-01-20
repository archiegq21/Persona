package com.apps.usergen.viewmodel

import app.cash.turbine.test
import com.apps.usergen.data.createNetworkUser
import com.apps.usergen.data.createUser
import com.apps.usergen.data.userCollectionEntity
import com.apps.usergen.repository.UserRepository
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserDetailViewModelTest {

    private val mockUserRepository = mock<UserRepository>(MockMode.autoUnit)

    private lateinit var viewModel: UserDetailViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = UserDetailViewModel(
            repository = mockUserRepository,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun given_CollectionId_then_GetUserState() = runTest {
        val user = createUser()
        every { mockUserRepository.getUserBy(any()) } returns flowOf(user)

        viewModel.getUser(userCollectionEntity.id).test { 
            assertEquals(UserState.Loading, awaitItem())
            assertEquals(UserState.Found(user), awaitItem())
        }
    }

}