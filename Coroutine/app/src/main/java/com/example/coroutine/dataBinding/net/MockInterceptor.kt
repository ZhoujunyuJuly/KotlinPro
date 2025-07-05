package com.example.coroutine.dataBinding.net

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val json = """
            {
                "id": 1,
                "name": "Mock User",
                "email": "222@qq.com"
            }
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message(json)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }
}
