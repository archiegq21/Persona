package com.apps.usergen.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.apps.database.dao.UserCollectionDao
import com.apps.database.data.UserCollectionEntity
import com.apps.usergen.data.UserCollection
import com.apps.usergen.data.toEntity
import com.apps.usergen.data.toModel
import kotlinx.coroutines.flow.map

internal class RealUserCollectionRepository(
    private val userCollectionDao: UserCollectionDao,
) : UserCollectionRepository {

    override fun getPagedUserCollection() = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = userCollectionDao::getPagedUserCollection,
    ).flow.map {
        it.map(UserCollectionEntity::toModel)
    }

    override fun addUserCollection(userCollection: UserCollection) {
        userCollectionDao.insertUserCollection(userCollection.toEntity())
    }
}