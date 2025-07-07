package com.example.coroutine.exPro.db

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    
    @Provides
    @Singleton
    fun getCakeDataBase(application: Application):CakeDatabase {
        return Room.databaseBuilder(application,
            CakeDatabase::class.java, "cake.db")
            .fallbackToDestructiveMigration(false)
            .build()
    }


    @Provides
    @Singleton
    fun getCakeDao(application: Application):CakeDao {
        return getCakeDataBase(application).getCakeDao()
    }


}