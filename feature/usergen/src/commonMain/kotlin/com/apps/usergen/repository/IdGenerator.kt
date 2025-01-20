package com.apps.usergen.repository

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface IdGenerator {
    fun generateId(): String
}

object UuidIdGenerator : IdGenerator {

    @OptIn(ExperimentalUuidApi::class)
    override fun generateId(): String {
        return Uuid.random().toHexString()
    }

}