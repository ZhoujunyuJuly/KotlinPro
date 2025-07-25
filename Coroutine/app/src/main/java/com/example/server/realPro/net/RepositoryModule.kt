package com.example.server.realPro.net

import com.example.server.realPro.db.AppDatabase
import com.example.server.realPro.mapper.CarEntity2ItemModel
import com.example.server.realPro.remote.CarBrandService
import com.example.server.realPro.repository.CarBrandRepositoryImp
import com.example.server.realPro.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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