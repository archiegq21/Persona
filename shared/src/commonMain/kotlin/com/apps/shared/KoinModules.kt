package com.apps.shared

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import org.koin.core.KoinApplication

val koinAppDeclaration: KoinApplication.() -> Unit = {
    logger(KermitKoinLogger(Logger.withTag("koin")))
    modules()
}