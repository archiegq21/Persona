package com.apps.database.di

import com.apps.database.PersonaDatabase
import com.apps.database.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module


internal actual fun Module.databasePlatformModules() {

    single<PersonaDatabase> {
        getDatabaseBuilder(androidContext()).build()
    }

}