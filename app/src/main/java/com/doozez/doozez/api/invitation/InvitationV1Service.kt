package com.doozez.doozez.api.invitation

import retrofit2.Call
import retrofit2.http.*

interface InvitationV1Service {
    @GET("v1/invitations/")
    fun getInvitations(): Call<List<InvitationDetailResponse>>

    @GET("v1/invitations/")
    fun getInvitationsForSafe(@Query("safe") safeId: Long): Call<List<InvitationDetailResponse>>

    @POST("v1/invitations/")
    fun createInvitation(@Body body: InvitationCreateRequest): Call<Void>

    @PATCH("v1/invitations/{id}/")
    fun updateInvitationForAction(@Path("id") id: Long, @Body body: InvitationActionReq): Call<InvitationActionResp>
}