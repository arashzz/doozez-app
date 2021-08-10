package com.doozez.doozez.api.safe

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SafeV1Service {

//    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @GET("v1/safes/")
    fun getSafesForUser(): Call<List<SafeDetailResp>>

//    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @POST("v1/safes/")
    fun createSafeForUser(@Body body: SafeCreateReq): Call<SafeDetailResp>

//    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @GET("v1/safes/{safeId}/")
    fun getSafeByIdForUser(@Path("safeId") safeId: Int): Call<SafeDetailResp>
}