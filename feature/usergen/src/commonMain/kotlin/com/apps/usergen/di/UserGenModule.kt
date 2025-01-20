package com.apps.usergen.di

import com.apps.usergen.repository.IdGenerator
import com.apps.usergen.repository.NetworkUserRemoteSource
import com.apps.usergen.repository.RealUserCollectionRepository
import com.apps.usergen.repository.RealUserRepository
import com.apps.usergen.repository.UserCollectionRepository
import com.apps.usergen.repository.UserRemoteSource
import com.apps.usergen.repository.UserRepository
import com.apps.usergen.repository.UuidIdGenerator
import com.apps.usergen.viewmodel.UserCollectionViewModel
import com.apps.usergen.viewmodel.UserDetailViewModel
import com.apps.usergen.viewmodel.UserGenRequestViewModel
import com.apps.usergen.viewmodel.UserListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val userGenModule = module {

    viewModel {
        UserGenRequestViewModel(
            idGenerator = get(),
            repository = get(),
            savedStateHandle = get(),
        )
    }

    viewModel {
        UserCollectionViewModel(
            repository = get(),
        )
    }

    viewModel { params ->
        UserListViewModel(
            collectionId = params.get(),
            repository = get(),
        )
    }

    viewModel {
        UserDetailViewModel(
            repository = get(),
        )
    }

    single<UserCollectionRepository> {
        RealUserCollectionRepository(
            userCollectionDao = get(),
        )
    }

    single<UserRepository> {
        RealUserRepository(
            userCollectionDao = get(),
            userDao = get(),
            userRemoteSource = get(),
        )
    }

    single<UserRemoteSource> {
        NetworkUserRemoteSource(
            httpClient = get(),
        )
    }

    single<IdGenerator> {
        UuidIdGenerator
    }
}