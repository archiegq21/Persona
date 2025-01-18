package com.apps.shared

import co.touchlab.crashkios.crashlytics.enableCrashlytics
import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Logger
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter
import co.touchlab.kermit.platformLogWriter

@OptIn(ExperimentalKermitApi::class)
fun setUpAndroidCrashlytics() {
    enableCrashlytics()
    Logger.setLogWriters(CrashlyticsLogWriter(), platformLogWriter())
}