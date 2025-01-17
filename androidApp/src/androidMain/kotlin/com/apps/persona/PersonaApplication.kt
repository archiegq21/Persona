package com.apps.persona

import android.app.Application
import com.apps.shared.setUpCrashlytics

class PersonaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpCrashlytics()
    }
}