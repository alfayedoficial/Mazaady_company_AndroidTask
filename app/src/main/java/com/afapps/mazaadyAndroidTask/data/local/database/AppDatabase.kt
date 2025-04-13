package com.afapps.mazaadyAndroidTask.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.afapps.mazaadyAndroidTask.data.local.dao.MovieDao
import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity

@Database(
    version = 3,
    entities = [
        MovieEntity::class
    ],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
