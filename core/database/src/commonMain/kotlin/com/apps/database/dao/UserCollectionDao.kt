package com.apps.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apps.database.data.UserCollectionEntity


@Dao
interface UserCollectionDao {
    @Query("SELECT * FROM UserCollection")
    fun getPagedUserCollection(): PagingSource<Int, UserCollectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCollection(userCollection: UserCollectionEntity)

    @Query("DELETE FROM UserCollection WHERE id = :id")
    suspend fun deleteUserCollectionBy(id: String)
}