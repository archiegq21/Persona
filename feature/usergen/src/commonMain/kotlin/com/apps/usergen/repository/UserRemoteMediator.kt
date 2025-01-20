package com.apps.usergen.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.apps.database.dao.UserDao
import com.apps.database.data.UserCollectionEntity
import com.apps.database.data.UserEntity
import com.apps.usergen.data.UserCollection
import com.apps.usergen.data.toEntity

@OptIn(ExperimentalPagingApi::class)
internal class UserRemoteMediator(
    private val userCollection: UserCollectionEntity,
    private val userDao: UserDao,
    private val remoteSource: UserRemoteSource,
) : RemoteMediator<Int, UserEntity>() {

    private var currentPage: Int = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        return try {
            currentPage = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> currentPage + 1
            }

            val userResponse = remoteSource.fetchUsers(
                seed = userCollection.id,
                page = currentPage,
                countPerPage = state.config.pageSize,
            )
            val userEntities = userResponse.results.map {
                it.toEntity(collectionId = userCollection.id)
            }

            val isLimitReached = currentPage * state.config.pageSize >= userCollection.count

            if (loadType == LoadType.REFRESH) {
                userDao.deleteAllAndInsertAll(
                    collectionId = userCollection.id,
                    users = userEntities,
                )
            } else {
                userDao.insertUsers(userEntities)
            }

            MediatorResult.Success(isLimitReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}