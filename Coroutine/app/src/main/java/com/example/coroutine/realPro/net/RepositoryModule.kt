package com.example.coroutine.realPro.net

import android.app.Application
import androidx.room.Room
import com.example.coroutine.realPro.db.AppDatabase
import com.example.coroutine.realPro.db.CarBrandDao
import com.example.coroutine.realPro.mapper.CarEntity2ItemModel
import com.example.coroutine.realPro.remote.CarBrandService
import com.example.coroutine.realPro.repository.CarBrandRepositoryImp
import com.example.coroutine.realPro.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAppDataBase(api:CarBrandService,
                           database: AppDatabase
    ): Repository{
        return CarBrandRepositoryImp(api,database, CarEntity2ItemModel())
    }

}