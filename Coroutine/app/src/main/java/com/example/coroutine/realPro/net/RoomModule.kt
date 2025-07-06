package com.example.coroutine.realPro.net

import android.app.Application
import androidx.room.Room
import com.example.coroutine.realPro.db.AppDatabase
import com.example.coroutine.realPro.db.CarBrandDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDataBase(application: Application):AppDatabase {
        return Room.databaseBuilder(application,
            AppDatabase::class.java,
            "car_home.db")
            .build()
    }

    @Singleton
    @Provides
    fun provideCarBrandDao(appDatabase: AppDatabase): CarBrandDao {
        return appDatabase.carBrandDao()
    }
}