package com.doozez.doozez.api.safe

import com.doozez.doozez.api.invitation.InvitationCreateRequest
import retrofit2.Call
import retrofit2.http.*

interface SafeV1Service {

    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @GET("v1/safes")
    fun getSafesForUser(): Call<List<SafeDetailResponse>>

    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @POST("v1/safes")
    fun createSafeForUser(@Body body: SafeCreateRequest): Call<Void>

    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @GET("v1/safes/{safeId}")
    fun getSafeByIdForUser(@Path("safeId") safeId: Long): Call<SafeDetailResponse>
}