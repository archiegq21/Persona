package com.apps.usergen.repository

import androidx.paging.PagingData
import com.apps.usergen.data.UserCollection
import kotlinx.coroutines.flow.Flow

interface UserCollectionRepository {
    fun getPagedUserCollection(): Flow<PagingData<UserCollection>>

    suspend fun addUserCollection(userCollection: UserCollection)
}