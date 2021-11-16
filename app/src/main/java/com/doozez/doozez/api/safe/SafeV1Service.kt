package com.doozez.doozez.api.safe

import com.doozez.doozez.api.PaginatedListResponse
import com.doozez.doozez.api.invitation.InviteActionReq
import com.doozez.doozez.api.invitation.InviteActionResp
import com.doozez.doozez.api.job.JobDetailResp
import retrofit2.Call
import retrofit2.http.*

interface SafeV1Service {

    @GET("v1/safes/")
    fun getSafesForUser(): Call<PaginatedListResponse<SafeDetailResp>>

    @POST("v1/safes/")
    fun createSafeForUser(@Body body: SafeCreateReq): Call<SafeDetailResp>

    @GET("v1/safes/{safeId}/")
    fun getSafeByIdForUser(@Path("safeId") safeId: Int): Call<SafeDetailResp>

    @PATCH("v1/safes/{id}/")
    fun updateSafeForAction(@Path("id") id: Int, @Body body: SafeActionReq): Call<SafeDetailResp>
}