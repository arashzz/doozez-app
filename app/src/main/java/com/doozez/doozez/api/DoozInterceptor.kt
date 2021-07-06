package com.doozez.doozez.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class DoozInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //admin
//        val header1 = "YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA"

        //arash
        val header2 = "YXJhc2hAZG9vemV6LmNvbTphcmFzaDEyM0A="

        val original = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
        requestBuilder.addHeader("Authorization", "Basic $header2")
        return chain.proceed(requestBuilder.build())
    }
}