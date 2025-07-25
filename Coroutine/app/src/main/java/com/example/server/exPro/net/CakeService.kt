package com.example.server.exPro.net

import com.example.server.exPro.net.model.CakeItemModel
import retrofit2.http.GET
import retrofit2.http.Query


interface CakeService {

    @GET("cake.do")
    suspend fun getCakeList(@Query("since")since :Int
                    ,@Query("pageSize")pageSize:Int): List<CakeItemModel>
}