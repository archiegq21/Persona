package com.apps.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.apps.database.data.UserCollectionEntity
import com.apps.database.data.UserEntity


@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE collectionId = :collectionId")
    fun getPagedUsers(collectionId: String): PagingSource<Int, UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(user: List<UserEntity>)

    @Query("DELETE FROM User WHERE collectionId = :collectionId")
    suspend fun deleteUsersBy(collectionId: String)

    @Transaction
    suspend fun deleteAllAndInsertAll(
        collectionId: String,
        users: List<UserEntity>,
    ) {
        deleteUsersBy(collectionId)
        insertUsers(users)
    }
}