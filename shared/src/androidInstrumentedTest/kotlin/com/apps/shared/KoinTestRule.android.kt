package com.apps.shared

import androidx.test.platform.app.InstrumentationRegistry
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

actual fun getKoinTestRule(modules: List<Module>): KoinTestRule = AndroidKoinTestRule(modules)

class AndroidKoinTestRule(
    override val modules: List<Module>
) : KoinTestRule {
    override fun starting() {
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
            modules(modules)
        }
    }

    override fun finished() {
        stopKoin()
    }
}