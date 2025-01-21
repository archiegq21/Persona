package com.apps.shared

import com.apps.database.di.databaseModule
import com.apps.network.di.networkModule
import com.apps.usergen.di.userGenModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module


actual fun initTestKoin(
    mockModule: Module,
) {
    startKoin {
        modules(
            databaseModule,
            userGenModule,
            networkModule,
            mockModule,
        )
    }
}