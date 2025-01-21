package com.apps.shared

import androidx.test.platform.app.InstrumentationRegistry
import com.apps.database.di.databaseModule
import com.apps.network.di.networkModule
import com.apps.usergen.di.userGenModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

actual fun initTestKoin(
    mockModule: Module,
) {
    startKoin {
        androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        modules(
            databaseModule,
            userGenModule,
            networkModule,
            mockModule,
        )
    }
}