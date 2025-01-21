package com.apps.shared

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.room.RoomDatabase
import com.apps.database.PersonaDatabase
import com.apps.usergen.data.UserResponse
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class PersonaAppTest : KoinTest {

    private val mockEngineProvider = mock<MockEngineProvider> {
        every { provide() } returns MockEngine {
            respondOk(content = "")
        }
    }

    private val database: PersonaDatabase by lazy(::get)

    @BeforeTest
    fun setup() {
        initTestKoin(
            mockModule = module {
                single<HttpClientEngine> {
                    mockEngineProvider.provide()
                }

                single<PersonaDatabase> {
                    getDatabaseBuilder().build()
                }
            }
        )
    }

    @AfterTest
    fun tearDown() {
        database.close()
        stopKoin()
    }

    @Test
    fun given_OnStartUpAndNoCollection_showEmptyState() = runComposeUiTest {
        setContent {
            PersonaApp()
        }

        onNodeWithText("Looks like you don't have any users yet")
            .assertIsDisplayed()

        onNodeWithText("Generate Users")
            .assertIsDisplayed()
    }

    @Test
    fun given_WhenCollectionAvailable_Then_FabIsDisplayed() = runComposeUiTest {
        runBlocking {
            database.getUserCollectionDao().insertUserCollection(testCollection)

            setContent {
                PersonaApp()
            }

            onAllNodesWithText("Generate Users")
                .onFirst()
                .isDisplayed()
        }
    }

    @Test
    fun given_WhenCollectionAvailable_Then_CollectionIsVisible() = runComposeUiTest {
        runBlocking {
            database.getUserCollectionDao().insertUserCollection(testCollection)

            setContent {
                PersonaApp()
            }

            onNodeWithText("Looks like you don't have any users yet")
                .assertIsNotDisplayed()

            onNodeWithText(testCollection.name)
                .assertIsDisplayed()
        }
    }

    @Test
    fun given_onGenerateUsers_Then_showRequestForm() = runComposeUiTest {
        setContent {
            PersonaApp()
        }

        onNodeWithText("Generate Users")
            .assertIsDisplayed()
            .performClick()

        onNodeWithText("Collection Name")
            .assertIsDisplayed()

        onNodeWithText("How many?")
            .assertIsDisplayed()
    }

    @Test
    fun given_GenerateUserWithInvalidInput_Then_ShowError() = runComposeUiTest {
        setContent {
            PersonaApp()
        }

        onNodeWithText("Generate Users")
            .assertIsDisplayed()
            .performClick()

        onNodeWithText("Collection Name")
            .assertIsDisplayed()
            .performTextInput(testCollection.name)

        onNodeWithText("How many?")
            .assertIsDisplayed()
            .performTextInput("asdasd")

        onNodeWithText("Hmm.. that's not a valid count")
            .assertIsDisplayed()

        onNodeWithText("Generate")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun given_GenerateUserWithZeroInput_Then_ShowError() = runComposeUiTest {
        setContent {
            PersonaApp()
        }

        onNodeWithText("Generate Users")
            .assertIsDisplayed()
            .performClick()

        onNodeWithText("Collection Name")
            .assertIsDisplayed()
            .performTextInput(testCollection.name)

        onNodeWithText("How many?")
            .assertIsDisplayed()
            .performTextInput("0")

        onNodeWithText("Ops! Zero isn't a valid count")
            .assertIsDisplayed()

        onNodeWithText("Generate")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun given_GenerateUserWithValidInput_Then_GenerateEnabled() = runComposeUiTest {
        setContent {
            PersonaApp()
        }

        onNodeWithText("Generate Users")
            .assertIsDisplayed()
            .performClick()

        onNodeWithText("Collection Name")
            .assertIsDisplayed()
            .performTextInput(testCollection.name)

        onNodeWithText("How many?")
            .assertIsDisplayed()
            .performTextInput(testCollection.count.toString())

        onNodeWithText("Generate")
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun given_GenerateUser_Then_ShowUserList() = runComposeUiTest {
        every { mockEngineProvider.provide() } returns MockEngine {
            respondOk(
                content = Json.encodeToString(
                    UserResponse.serializer(),
                    UserResponse(
                        results = buildList {
                            repeat(testCollection.count) {
                                add(createNetworkUser(it.toString()))
                            }
                        },
                        info = UserResponse.Info(
                            seed = testCollection.id,
                            results = testCollection.count,
                            page = 1,
                            version = "1.4",
                        )
                    )
                )
            )
        }

        setContent {
            PersonaApp()
        }

        onNodeWithText("Generate Users")
            .performClick()

        onNodeWithText("Collection Name")
            .performTextInput(testCollection.name)

        onNodeWithText("How many?")
            .performTextInput(testCollection.count.toString())

        onNodeWithText("Generate")
            .performClick()

        waitForIdle()

        onNodeWithText(testCollection.name)
            .assertIsDisplayed()

        onAllNodesWithText("Title First Last")
            .onFirst()
            .isDisplayed()
    }

}