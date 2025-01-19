package com.apps.database.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apps.model.Gender

@Entity(tableName = "UserCollection")
data class UserCollectionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val count: Int,
)
