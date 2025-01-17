package com.apps.shared

import co.touchlab.crashkios.crashlytics.enableCrashlytics
import co.touchlab.crashkios.crashlytics.setCrashlyticsUnhandledExceptionHook
import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Logger
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter
import co.touchlab.kermit.platformLogWriter

@Suppress("unused")
@OptIn(ExperimentalKermitApi::class)
fun setUpCrashlytics() {
    enableCrashlytics()
    setCrashlyticsUnhandledExceptionHook()
    Logger.setLogWriters(CrashlyticsLogWriter(), platformLogWriter())
}