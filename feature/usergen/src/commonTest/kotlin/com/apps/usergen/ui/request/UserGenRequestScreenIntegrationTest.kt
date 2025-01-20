package com.apps.usergen.ui.request

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.apps.database.di.databaseModule
import com.apps.network.di.networkModule
import com.apps.usergen.di.userGenModule
import com.apps.usergen.getKoinTestRule
import com.apps.usergen.viewmodel.GenUserParams
import com.apps.usergen.viewmodel.UserGenRequestViewModel
import dev.mokkery.MockMode
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import org.koin.compose.viewmodel.koinViewModel
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class UserGenRequestScreenIntegrationTest : KoinTest {

    private val mockOnClose: () -> Unit = mock(MockMode.autoUnit)
    private val mockGenerateUsers: (GenUserParams) -> Unit = mock(MockMode.autoUnit)

    private val koinTestRule by lazy {
        getKoinTestRule(
            listOf(
                databaseModule,
                userGenModule,
                networkModule,
            )
        )
    }

    private lateinit var viewModel: UserGenRequestViewModel

    @BeforeTest
    fun setup() {
        koinTestRule.starting()
    }

    @AfterTest
    fun tearDown() {
        koinTestRule.finished()
    }

    @Test
    fun given_ScreenLoaded_Then_FieldMustBeVisible() = runComposeUiTest {
        setContent {
            viewModel = koinViewModel()

            UserGenRequestRoute(
                viewModel = viewModel,
                onClose = mockOnClose,
                generateUsers = mockGenerateUsers,
                modifier = Modifier.fillMaxSize(),
            )
        }

        onNodeWithText("Collection Name").isDisplayed()
        onNodeWithText("How many?").isDisplayed()
    }

    @Test
    fun given_ScreenLoaded_Then_GenerateIsDisabled() = runComposeUiTest {
        setContent {
            viewModel = koinViewModel()

            UserGenRequestRoute(
                viewModel = viewModel,
                onClose = mockOnClose,
                generateUsers = mockGenerateUsers,
                modifier = Modifier.fillMaxSize(),
            )
        }

        onNodeWithText("Generate")
            .assertIsNotEnabled()
            .isDisplayed()
    }

    @Test
    fun given_Count_Then_GenerateIsEnabled() = runComposeUiTest {
        setContent {
            viewModel = koinViewModel()

            UserGenRequestRoute(
                viewModel = viewModel,
                onClose = mockOnClose,
                generateUsers = mockGenerateUsers,
                modifier = Modifier.fillMaxSize(),
            )
        }

        onNodeWithText("How many?")
            .performTextInput("10")

        onNodeWithText("Generate")
            .assertIsEnabled()
            .isDisplayed()
    }

    @Test
    fun given_InvalidCount_Then_ShowError() = runComposeUiTest {
        setContent {
            viewModel = koinViewModel()

            UserGenRequestRoute(
                viewModel = viewModel,
                onClose = mockOnClose,
                generateUsers = mockGenerateUsers,
                modifier = Modifier.fillMaxSize(),
            )
        }

        onNodeWithText("How many?")
            .performTextInput("asda")

        onNodeWithText("Generate")
            .assertIsNotEnabled()

        onNodeWithText("Hmm.. that's not a valid count")
            .isDisplayed()
    }

    @Test
    fun given_ZeroCount_Then_ShowError() = runComposeUiTest {
        setContent {
            viewModel = koinViewModel()

            UserGenRequestRoute(
                viewModel = viewModel,
                onClose = mockOnClose,
                generateUsers = mockGenerateUsers,
                modifier = Modifier.fillMaxSize(),
            )
        }

        onNodeWithText("How many?")
            .performTextInput("0")

        onNodeWithText("Generate")
            .assertIsNotEnabled()

        onNodeWithText("Ops! Zero isn't a valid count")
            .isDisplayed()
    }

    @Test
    fun given_GenerateUser_Then_PassedParams() = runComposeUiTest {
        setContent {
            viewModel = koinViewModel()

            UserGenRequestRoute(
                viewModel = viewModel,
                onClose = mockOnClose,
                generateUsers = mockGenerateUsers,
                modifier = Modifier.fillMaxSize(),
            )
        }

        onNodeWithText("Collection Name")
            .performTextInput("100")

        onNodeWithText("How many?")
            .performTextInput("10")

        onNodeWithText("Generate")
            .performClick()

        waitForIdle()

        verify(VerifyMode.exhaustive) {
            mockGenerateUsers(
                GenUserParams(
                    id = any(),
                    name = "10",
                )
            )
        }
    }

}