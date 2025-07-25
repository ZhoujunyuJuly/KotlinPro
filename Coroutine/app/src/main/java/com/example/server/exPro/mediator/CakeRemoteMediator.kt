package com.example.server.exPro.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.server.exPro.db.CakeDatabase
import com.example.server.exPro.net.CakeService
import com.example.server.exPro.net.model.CakeEntity

@OptIn(ExperimentalPagingApi::class)
class CakeRemoteMediator (val database: CakeDatabase, val cakeApi: CakeService)
    : RemoteMediator<Int,CakeEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CakeEntity>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND ->{
                state.lastItemOrNull()?.page?:0
            }
        }?:1

        val result = cakeApi.getCakeList(page * state.pages.size, state.config.pageSize)
        val dbResult = result.map {
            CakeEntity(it.name,it.icon,page + 1)
        }

        if(result.isNotEmpty()){
            database.getCakeDao().insertCakeList(dbResult)
        }

        return MediatorResult.Success(true)
    }
}