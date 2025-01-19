package com.apps.usergen.repository

import com.apps.model.Gender
import com.apps.usergen.data.UserResponse

interface UserRemoteSource {
    suspend fun fetchUsers(
        gender: Gender?,
        seed: String,
        page: Int,
        countPerPage: Int,
    ): UserResponse
}