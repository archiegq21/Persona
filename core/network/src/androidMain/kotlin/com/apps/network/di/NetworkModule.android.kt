package com.apps.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import org.koin.core.module.Module

internal actual fun Module.networkPlatformModules() {

    single<HttpClientEngine> {
        OkHttpEngine(OkHttpConfig())
    }

}