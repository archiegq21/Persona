package com.apps.shared

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


fun initAndroidKoin(context: Context) {
    startKoin {
        androidContext(context)
        koinAppDeclaration()
    }
}