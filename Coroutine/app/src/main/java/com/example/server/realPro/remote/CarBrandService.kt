package com.example.server.realPro.remote

import com.example.server.realPro.model.CarBrandItemModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CarBrandService {

    @GET("carBrand")
    suspend fun fetchData(
        @Query("since") since:Int,
        @Query("pageSize") pageSize:Int
    ):List<CarBrandItemModel>
}