package com.apps.database.converts

import androidx.room.TypeConverter
import com.apps.database.data.UserEntity
import kotlinx.serialization.json.Json

class UserEntityConverter {
    @TypeConverter
    fun fromLocationString(value: String): UserEntity.Location {
        return Json.decodeFromString(UserEntity.Location.serializer(), value)
    }

    @TypeConverter
    fun fromLocation(value: UserEntity.Location): String {
        return Json.encodeToString(UserEntity.Location.serializer(), value)
    }

    @TypeConverter
    fun fromCoordinatesString(value: String): UserEntity.Coordinates {
        return Json.decodeFromString(UserEntity.Coordinates.serializer(), value)
    }

    @TypeConverter
    fun fromCoordinates(value: UserEntity.Coordinates): String {
        return Json.encodeToString(UserEntity.Coordinates.serializer(), value)
    }

    @TypeConverter
    fun fromTimezoneString(value: String): UserEntity.Timezone {
        return Json.decodeFromString(UserEntity.Timezone.serializer(), value)
    }

    @TypeConverter
    fun fromTimezone(value: UserEntity.Timezone): String {
        return Json.encodeToString(UserEntity.Timezone.serializer(), value)
    }

    @TypeConverter
    fun fromStreetString(value: String): UserEntity.Street {
        return Json.decodeFromString(UserEntity.Street.serializer(), value)
    }

    @TypeConverter
    fun fromTimezone(value: UserEntity.Street): String {
        return Json.encodeToString(UserEntity.Street.serializer(), value)
    }
}