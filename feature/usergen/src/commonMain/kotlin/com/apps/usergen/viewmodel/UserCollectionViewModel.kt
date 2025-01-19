package com.apps.usergen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.apps.usergen.data.UserCollection
import com.apps.usergen.repository.UserCollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserCollectionViewModel(
    repository: UserCollectionRepository,
): ViewModel() {

    val pagedUserCollection: Flow<PagingData<UserCollection>> =
        repository.getPagedUserCollection()
            .map { it }.cachedIn(viewModelScope)

}