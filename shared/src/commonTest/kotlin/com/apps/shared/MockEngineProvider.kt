package com.apps.shared

import io.ktor.client.engine.HttpClientEngine

interface MockEngineProvider {
    fun provide(): HttpClientEngine
}