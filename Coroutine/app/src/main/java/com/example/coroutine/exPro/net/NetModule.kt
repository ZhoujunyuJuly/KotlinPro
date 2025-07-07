package com.example.coroutine.exPro.net

import android.app.Application
import com.example.coroutine.realPro.net.CarMockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

//    @Provides
//    @Singleton
//    fun getOkHttpClient():OkHttpClient{
//        return OkHttpClient.Builder().addInterceptor(CarMockInterceptor()).build()
//    }
//
//    @Provides
//    @Singleton
//    fun getRetrofit():Retrofit{
//        return Retrofit.Builder()
//            .client(getOkHttpClient())
//            .baseUrl("https://dd")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

    @Provides
    @Singleton
    fun getCakeService(retrofit: Retrofit):CakeService{
        return retrofit.create(CakeService::class.java)
    }
}