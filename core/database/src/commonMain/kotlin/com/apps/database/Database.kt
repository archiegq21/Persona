package com.apps.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.apps.database.dao.UserCollectionDao
import com.apps.database.data.UserCollectionEntity

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal expect object PersonaDatabaseConstructor : RoomDatabaseConstructor<PersonaDatabase> {
    override fun initialize(): PersonaDatabase
}

@Database(
    entities = [
        UserCollectionEntity::class,
    ], version = 1
)
@ConstructedBy(PersonaDatabaseConstructor::class)
abstract class PersonaDatabase : RoomDatabase() {
    abstract fun getUserCollectionDao(): UserCollectionDao
}
