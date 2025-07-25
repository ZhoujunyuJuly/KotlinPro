package com.example.server.exPro.repository

import com.example.server.exPro.db.CakeDatabase
import com.example.server.exPro.mapper.CakeMapperImp
import com.example.server.exPro.net.CakeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CakeRepositoryModule {

    @Provides
    @Singleton
    fun provideCakeRepository(api:CakeService,database:CakeDatabase):CakeRepository{
        return CakeRepositoryImp(database,api, CakeMapperImp())
    }
}