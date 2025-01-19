package com.apps.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Gender {
    @SerialName("male") Male,
    @SerialName("female") Female
}