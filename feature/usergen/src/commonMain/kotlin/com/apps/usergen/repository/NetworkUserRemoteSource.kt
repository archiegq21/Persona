package com.apps.usergen.repository

import com.apps.model.Gender
import com.apps.usergen.data.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json

internal class NetworkUserRemoteSource(
    private val httpClient: HttpClient,
) : UserRemoteSource {

    override suspend fun fetchUsers(
        seed: String,
        page: Int,
        countPerPage: Int,
    ): UserResponse = httpClient.get {
        parameter("page", page)
        parameter("results", countPerPage)
        parameter("seed", seed)
    }.body<UserResponse>()

}