package com.example.server.realPro.repository

import androidx.paging.PagingData
import com.example.server.realPro.model.CarBrandItemModel
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun fetchCarBrandList(): Flow<PagingData<CarBrandItemModel>>
}