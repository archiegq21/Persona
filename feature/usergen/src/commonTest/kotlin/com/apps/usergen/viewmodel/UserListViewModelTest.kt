package com.apps.usergen.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.apps.usergen.data.userCollectionEntity
import com.apps.usergen.repository.UserRepository
import dev.mokkery.MockMode
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class UserListViewModelTest {

    private val mockUserRepository = mock<UserRepository>(MockMode.autoUnit)

    private lateinit var viewModel: UserListViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = UserListViewModel(
            collectionId = userCollectionEntity.id,
            repository = mockUserRepository,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }



}