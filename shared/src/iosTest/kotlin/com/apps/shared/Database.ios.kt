package com.apps.shared

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.apps.database.PersonaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual fun getDatabaseBuilder(): RoomDatabase.Builder<PersonaDatabase> {
    return Room.inMemoryDatabaseBuilder<PersonaDatabase>()
        .setDriver(BundledSQLiteDriver()) // Very important
        .setQueryCoroutineContext(Dispatchers.IO)
}