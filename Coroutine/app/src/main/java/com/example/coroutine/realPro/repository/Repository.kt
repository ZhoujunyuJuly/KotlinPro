package com.example.coroutine.realPro.repository

import androidx.paging.PagingData
import com.example.coroutine.realPro.model.CarBrandItemModel
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun fetchCarBrandList(): Flow<PagingData<CarBrandItemModel>>
}