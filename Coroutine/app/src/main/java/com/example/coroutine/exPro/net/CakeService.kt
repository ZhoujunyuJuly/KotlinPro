package com.example.coroutine.exPro.net

import com.example.coroutine.exPro.net.model.CakeItemModel
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject


interface CakeService {

    @GET("cake.do")
    suspend fun getCakeList(@Query("since")since :Int
                    ,@Query("pageSize")pageSize:Int): List<CakeItemModel>
}