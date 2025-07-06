package com.example.coroutine.realPro.remote

import com.example.coroutine.realPro.model.CarBrandItemModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CarBrandService {

    @GET("carBrand")
    suspend fun fetchData(
        @Query("since") since:Int,
        @Query("pageSize") pageSize:Int
    ):List<CarBrandItemModel>
}