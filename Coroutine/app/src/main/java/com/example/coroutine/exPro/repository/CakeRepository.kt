package com.example.coroutine.exPro.repository

import androidx.paging.PagingData
import com.example.coroutine.exPro.db.CakeDatabase
import com.example.coroutine.exPro.net.CakeService
import com.example.coroutine.exPro.net.model.CakeItemModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface CakeRepository {

    fun getCakeList():Flow<PagingData<CakeItemModel>>
}