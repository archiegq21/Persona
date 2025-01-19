package com.apps.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module

internal actual fun Module.networkPlatformModules() {

    single<HttpClientEngine> {
        Darwin.create {

        }
    }

}