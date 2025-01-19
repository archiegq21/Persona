package com.apps.usergen.repository

import androidx.paging.PagingData
import com.apps.usergen.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserBy(userId: String): Flow<User>

    fun getPagedUsersForCollection(collectionId: String): Flow<PagingData<User>>
}