package com.apps.usergen

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

actual fun getKoinTestRule(modules: List<Module>): KoinTestRule = IosKoinTestRule(modules)

class IosKoinTestRule(
    override val modules: List<Module>
): KoinTestRule {

    override fun starting() {
        startKoin {
            modules(modules)
        }
    }

    override fun finished() {
        stopKoin()
    }

}