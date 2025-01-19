package com.apps.usergen.di

import com.apps.usergen.repository.RealUserCollectionRepository
import com.apps.usergen.repository.UserCollectionRepository
import com.apps.usergen.viewmodel.UserCollectionViewModel
import com.apps.usergen.viewmodel.UserGenRequestViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val userGenModule = module {

    viewModel {
        UserGenRequestViewModel(
            savedStateHandle = get(),
        )
    }

    viewModel {
        UserCollectionViewModel(
            repository = get(),
        )
    }

    single<UserCollectionRepository> {
        RealUserCollectionRepository(
            userCollectionDao = get(),
        )
    }
}