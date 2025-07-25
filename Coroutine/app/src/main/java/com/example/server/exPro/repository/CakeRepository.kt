package com.example.server.exPro.repository

import androidx.paging.PagingData
import com.example.server.exPro.net.model.CakeItemModel
import kotlinx.coroutines.flow.Flow

interface CakeRepository {

    fun getCakeList():Flow<PagingData<CakeItemModel>>
}