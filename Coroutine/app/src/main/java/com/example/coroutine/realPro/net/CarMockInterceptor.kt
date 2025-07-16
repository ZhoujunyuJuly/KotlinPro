package com.example.coroutine.realPro.net

import com.example.coroutine.realPro.model.CommonConstant
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class CarMockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Thread.sleep(2000)
        val request = chain.request()
        val url = request.url
        val since = url.queryParameter("since")?.toIntOrNull()?:0
        val pageSize = url.queryParameter("pageSize")?.toIntOrNull()?:8
        val item = (1..100).map {
            """{"id": $it, "name": "User Car$it", "icon": "${CommonConstant.IMAGE_URL}"}"""
        }
        val toIndex = (since + pageSize).coerceAtMost(item.size)
        val pageItems = item.subList(since,toIndex).joinToString (",\n" )
        val json = "[$pageItems]".trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }
}
