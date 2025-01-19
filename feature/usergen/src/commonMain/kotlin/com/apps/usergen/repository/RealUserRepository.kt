package com.apps.usergen.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.apps.database.dao.UserCollectionDao
import com.apps.database.dao.UserDao
import com.apps.usergen.data.User
import com.apps.usergen.data.toModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class RealUserRepository(
    private val userRemoteSource: UserRemoteSource,
    private val userCollectionDao: UserCollectionDao,
    private val userDao: UserDao,
): UserRepository {

    override fun getUserBy(userId: String): Flow<User> =
        userDao.getUserBy(userId).map { it.toModel() }

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    override fun getPagedUsersForCollection(collectionId: String): Flow<PagingData<User>> =
        userCollectionDao.getUserCollectionBy(collectionId).flatMapLatest { userCollection ->
            Pager(
                config = PagingConfig(pageSize = 10),
                remoteMediator = UserRemoteMediator(
                    userCollection = userCollection,
                    remoteSource = userRemoteSource,
                    userDao = userDao,
                ),
                pagingSourceFactory = { userDao.getPagedUsers(collectionId) },
            ).flow.map {
                it.map { userEntity -> userEntity.toModel() }
            }
        }


}