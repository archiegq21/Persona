package com.apps.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.apps.database.converts.LocalDateTimeConverter
import com.apps.database.converts.UserEntityConverter
import com.apps.database.dao.UserCollectionDao
import com.apps.database.dao.UserDao
import com.apps.database.data.UserCollectionEntity
import com.apps.database.data.UserEntity

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal expect object PersonaDatabaseConstructor : RoomDatabaseConstructor<PersonaDatabase> {
    override fun initialize(): PersonaDatabase
}

@Database(
    entities = [
        UserCollectionEntity::class,
        UserEntity::class,
    ], version = 1
)
@TypeConverters(
    LocalDateTimeConverter::class,
    UserEntityConverter::class,
)
@ConstructedBy(PersonaDatabaseConstructor::class)
abstract class PersonaDatabase : RoomDatabase() {
    abstract fun getUserCollectionDao(): UserCollectionDao
    abstract fun getUserDao(): UserDao
}
