package com.apps.shared

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.platform.app.InstrumentationRegistry
import com.apps.database.PersonaDatabase
import kotlinx.coroutines.Dispatchers

actual fun getDatabaseBuilder(): RoomDatabase.Builder<PersonaDatabase> {
    return Room.inMemoryDatabaseBuilder<PersonaDatabase>(
        context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext,
    ).setQueryCoroutineContext(Dispatchers.IO)
}