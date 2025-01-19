package com.apps.usergen.data

import com.apps.database.data.UserCollectionEntity

data class UserCollection(
    val id: String,
    val name: String,
    val count: Int,
)

fun UserCollectionEntity.toModel() = UserCollection(
    id = id,
    name = name,
    count = count,
)

fun UserCollection.toEntity() = UserCollectionEntity(
    id = id,
    name = name,
    count = count,
)