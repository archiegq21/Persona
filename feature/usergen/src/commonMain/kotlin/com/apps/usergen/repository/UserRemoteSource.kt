package com.apps.usergen.repository

import com.apps.usergen.data.UserResponse

interface UserRemoteSource {
    suspend fun fetchUsers(
        seed: String,
        page: Int,
        countPerPage: Int,
    ): UserResponse
}