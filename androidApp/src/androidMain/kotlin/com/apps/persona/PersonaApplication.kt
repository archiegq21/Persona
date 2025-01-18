package com.apps.persona

import android.app.Application
import com.apps.shared.setUpAndroidCrashlytics

class PersonaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpAndroidCrashlytics()
    }

}