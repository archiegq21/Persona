package com.apps.shared

import androidx.room.RoomDatabase
import com.apps.database.PersonaDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<PersonaDatabase>