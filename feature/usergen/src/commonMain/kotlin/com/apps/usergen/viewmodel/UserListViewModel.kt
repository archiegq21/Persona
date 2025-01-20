package com.apps.usergen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.apps.usergen.data.User
import com.apps.usergen.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserListViewModel(
    collectionId: String,
    repository: UserRepository,
) : ViewModel() {

    val pagedUsers: Flow<PagingData<User>> =
        repository.getPagedUsersForCollection(collectionId)
            .cachedIn(viewModelScope)

}