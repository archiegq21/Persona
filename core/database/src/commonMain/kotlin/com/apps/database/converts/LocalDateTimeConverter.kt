package com.apps.database.converts;

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.datetime.Instant

@ProvidedTypeConverter
object LocalDateTimeConverter {
    @TypeConverter
    fun fromInstant(value: Long): Instant {
        return value.let { Instant.fromEpochMilliseconds(value) }
    }

    @TypeConverter
    fun instantToString(date: Instant): Long {
        return date.toEpochMilliseconds()
    }
}