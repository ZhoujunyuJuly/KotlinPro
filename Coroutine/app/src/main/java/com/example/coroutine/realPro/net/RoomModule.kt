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

/**
 * 这个没有使用@Inject constructor 的形式，是因为需要用到 application
 * 🌟application 在单例作用域 SingletonComponent 会自动生成
 * 🌟@Inject 作用在没有入参的情况
 */
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