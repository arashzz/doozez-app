package com.doozez.doozez.api.safe

import com.doozez.doozez.api.invitation.InvitationCreateRequest
import retrofit2.Call
import retrofit2.http.*

interface SafeV1Service {

    @GET("v1/safes")
    fun getSafesForUser(@Query("userId") userId: String): Call<List<SafeDetailResponse>>

    @POST("v1/safes")
    fun createSafeForUser(@Body body: SafeCreateRequest): Call<Void>

    @GET("v1/safes/{safeId}")
    fun getSafeByIdForUser(@Path("safeId") safeId: Long, @Query("userId") userId: Long, ): Call<SafeDetailResponse>
}