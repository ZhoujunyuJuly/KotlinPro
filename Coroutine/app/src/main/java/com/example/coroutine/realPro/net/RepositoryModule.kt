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

/**
 * 这里其实可以使用class @Binds 的方法，因为两个入参 Hilt 都可以通过 @Provides 找到
 * 对于@Binds的情况，必须是abstract来实现，因为只写了返回类型，没有方法体，并不实例化对象
 * 这一步交给了Hilt,后台自动生成 @Provides
 */
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