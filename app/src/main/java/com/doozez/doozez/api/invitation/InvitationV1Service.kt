package com.doozez.doozez.api.invitation

import retrofit2.Call
import retrofit2.http.*

interface InvitationV1Service {
    @GET("v1/invitations/")
    fun getInvitations(): Call<List<InviteDetailResp>>

    @GET("v1/invitations/")
    fun getInvitationsForSafe(@Query("safe") safeId: Int): Call<List<InviteDetailResp>>

    @POST("v1/invitations/")
    fun createInvitation(@Body body: InviteCreateReq): Call<Void>

    @PATCH("v1/invitations/{id}/")
    fun updateInvitationForAction(@Path("id") id: Int, @Body body: InviteActionReq): Call<InviteActionResp>
}