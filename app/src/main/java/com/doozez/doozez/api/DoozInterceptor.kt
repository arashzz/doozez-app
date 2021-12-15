package com.doozez.doozez.api

import com.doozez.doozez.enums.SharedPrerfKey
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class DoozInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()

        val apiKey = SharedPrefManager.getString(SharedPrerfKey.API_KEY.name, null, true)
        if (!apiKey.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Token $apiKey")
        }
        return chain.proceed(requestBuilder.build())
    }
}