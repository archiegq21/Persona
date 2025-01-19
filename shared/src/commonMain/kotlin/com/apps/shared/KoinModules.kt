package com.apps.shared

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.apps.database.di.databaseModule
import com.apps.network.di.networkModule
import com.apps.usergen.di.userGenModule
import org.koin.core.KoinApplication

val koinAppDeclaration: KoinApplication.() -> Unit = {
    logger(KermitKoinLogger(Logger.withTag("koin")))
    modules(
        databaseModule,
        userGenModule,
        networkModule,
    )
}