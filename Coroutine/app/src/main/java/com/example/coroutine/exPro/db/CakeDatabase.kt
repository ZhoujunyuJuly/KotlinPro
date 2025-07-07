package com.example.coroutine.exPro.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coroutine.exPro.net.model.CakeEntity

@Database(entities = [CakeEntity::class], version = 2, exportSchema = false)
abstract class CakeDatabase:RoomDatabase() {

    abstract fun getCakeDao():CakeDao
}