package com.apps.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule = module {

    single {
        HttpClient(get<HttpClientEngine>()) {
            expectSuccess = true
            defaultRequest {
                contentType(ContentType.Application.Json)
                url("https://randomuser.me/api/1.4")
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = true
                        ignoreUnknownKeys = true
                        isLenient = true
                        allowSpecialFloatingPointValues = true
                        allowStructuredMapKeys = true
                        prettyPrint = true
                        useArrayPolymorphism = false
                    }
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        if (message.length > 4000) {
                            val chunkCount: Int = message.length / 4000
                            for (i in 0..chunkCount) {
                                val max = 4000 * (i + 1)
                                if (max >= message.length) {
                                    co.touchlab.kermit.Logger.i(
                                        message.substring(4000 * i),
                                        tag = "http"
                                    )
                                } else {
                                    co.touchlab.kermit.Logger.i(
                                        message.substring(4000 * i, max),
                                        tag = "http"
                                    )
                                }
                            }
                        } else {
                            co.touchlab.kermit.Logger.i(message, tag = "http")
                        }
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    networkPlatformModules()
}

internal expect fun Module.networkPlatformModules()