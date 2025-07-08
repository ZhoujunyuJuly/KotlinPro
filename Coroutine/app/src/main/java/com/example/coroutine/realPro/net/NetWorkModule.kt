package com.example.coroutine.realPro.net

import android.util.Log
import com.example.coroutine.dataBinding.net.MockInterceptor
import com.example.coroutine.realPro.model.CommonConstant.TAG
import com.example.coroutine.realPro.remote.CarBrandService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * 这个没有使用@Inject 是因为 OkHttpClient 和 Retrofit 都是第三方库
 * 无法在本类上写@Inject
 */
@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule{

    @Singleton
    @Provides
    fun provideOkHttpClient():OkHttpClient{
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideRetrofit():Retrofit{
        val interceptor = HttpLoggingInterceptor(){
            Log.d(TAG,it)
        }

        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .client(OkHttpClient.Builder()
                .addInterceptor(CarMockInterceptor())
                .build())
            .baseUrl("http://mock.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideCarBrandService(retrofit: Retrofit): CarBrandService {
        return retrofit.create(CarBrandService::class.java)
    }
}