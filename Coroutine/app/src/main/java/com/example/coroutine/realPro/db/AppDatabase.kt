package com.example.coroutine.realPro.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coroutine.realPro.model.CarBrandEntity

@Database(
    entities = [CarBrandEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun carBrandDao():CarBrandDao
}