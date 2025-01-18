package com.apps.shared

import org.koin.core.context.startKoin


@Suppress("unused")
fun initKoin() {
    startKoin {
        koinAppDeclaration()
    }
}