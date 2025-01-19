package com.apps.database.di

import com.apps.database.PersonaDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule = module {

    single {
        get<PersonaDatabase>().getUserCollectionDao()
    }

    databasePlatformModules()

}

internal expect fun Module.databasePlatformModules()