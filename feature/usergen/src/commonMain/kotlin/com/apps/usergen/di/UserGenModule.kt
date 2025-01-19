package com.apps.usergen.di

import com.apps.usergen.viewmodel.UserGenRequestViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val userGenModule = module {

    viewModel {
        UserGenRequestViewModel(
            savedStateHandle = get(),
        )
    }

}