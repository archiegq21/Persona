package com.apps.usergen.repository

import com.apps.usergen.data.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRemoteSourceTest {

    private lateinit var remoteSource: UserRemoteSource

    private fun getClient(engine: HttpClientEngine) = HttpClient(engine) {
        expectSuccess = true
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun when_onRequestUser_then_parametersShouldBeCorrect() = runTest {
        remoteSource = NetworkUserRemoteSource(
            httpClient = getClient(MockEngine { request ->
                assertEquals("", request.url.encodedPath)
                assertEquals("GET", request.method.value)
                assertEquals("1", request.url.parameters["page"])
                assertEquals("10", request.url.parameters["results"])
                assertEquals("Test", request.url.parameters["seed"])
                respond(
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    content = Json.encodeToString(
                        UserResponse.serializer(),
                        UserResponse(
                            info = UserResponse.Info(
                                seed = "Test",
                                results = 10,
                                page = 1,
                                version = "1.4",
                            ),
                            results = emptyList(),
                        )
                    ),
                )
            })
        )

        remoteSource.fetchUsers(
            seed = "Test",
            page = 1,
            countPerPage = 10
        )
    }
}