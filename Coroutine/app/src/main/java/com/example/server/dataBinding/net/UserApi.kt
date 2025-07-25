package com.example.server.dataBinding.net

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

data class User(val id : String,val name: String, val email: String)

interface UserServiceApi {
    @GET("user")
    fun loadUser(): Call<User>

    /**
     * 对于retrofit挂起的线程，如果在主线程作用域，会自动开启I/O线程执行
     */
    @GET("user")
    suspend fun getUser(): User
}

fun provideUserServiceApi(): UserServiceApi {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                //通过拦截模拟服务器数据
                .addInterceptor(MockInterceptor())
                .build()
        )
        .baseUrl("https://mock.local/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    return retrofit.create(UserServiceApi::class.java)
}

/**
 * 用不了
 */
fun getMockUrl(): String {
    val mockWebServer = MockWebServer()

    // 模拟返回的 JSON 数据
    val fakeJson = """
    {
        "id": 1,
        "name": "Mock User",
        "email": "mock@example.com"
    }
    """.trimIndent()

    // 配置响应
    mockWebServer.enqueue(
        MockResponse()
            .setResponseCode(200)
            .setBody(fakeJson)
    )

    mockWebServer.start()

    return mockWebServer.url("/").toString()
}