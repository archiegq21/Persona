package com.apps.usergen.data

import com.apps.database.data.UserCollectionEntity
import com.apps.model.Gender

data class UserCollection(
    val id: String,
    val count: Int,
    val gender: Gender?,
)

fun UserCollectionEntity.toModel() = UserCollection(
    id = id,
    count = count,
    gender = gender,
)