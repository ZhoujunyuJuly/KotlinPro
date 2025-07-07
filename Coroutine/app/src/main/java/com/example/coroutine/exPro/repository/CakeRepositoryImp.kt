package com.example.coroutine.exPro.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.coroutine.exPro.db.CakeDatabase
import com.example.coroutine.exPro.mapper.CakeMapperImp
import com.example.coroutine.exPro.mediator.CakeRemoteMediator
import com.example.coroutine.exPro.net.CakeService
import com.example.coroutine.exPro.net.model.CakeItemModel
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


class CakeRepositoryImp(private val cakeDatabase: CakeDatabase,
                        private val cakeApi: CakeService,
                        private val convertMapper:CakeMapperImp)
    : CakeRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCakeList(): Flow<PagingData<CakeItemModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 8,
                prefetchDistance = 5,
                initialLoadSize = 15
            ),
            remoteMediator = CakeRemoteMediator(cakeDatabase,cakeApi)
        ){
           cakeDatabase.getCakeDao().getCake()
        }.flow
            .flowOn(Dispatchers.IO)
            .map { pagingData->
                    pagingData.map {
                        convertMapper.convert(it)
            }
        }
    }
}