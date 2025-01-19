package com.apps.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apps.database.data.UserCollectionEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserCollectionDao {
    @Query("SELECT * FROM UserCollection")
    fun getPagedUserCollection(): PagingSource<Int, UserCollectionEntity>

    @Query("SELECT * FROM UserCollection WHERE id = :id")
    fun getUserCollectionBy(id: String): Flow<UserCollectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCollection(userCollection: UserCollectionEntity)

    @Query("DELETE FROM UserCollection WHERE id = :id")
    suspend fun deleteUserCollectionBy(id: String)
}