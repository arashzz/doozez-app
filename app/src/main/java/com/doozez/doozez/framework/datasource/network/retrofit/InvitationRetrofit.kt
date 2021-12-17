package com.doozez.doozez.framework.datasource.network.retrofit

import com.doozez.doozez.api.PaginatedListResponse
import com.doozez.doozez.api.invitation.InviteActionReq
import com.doozez.doozez.api.invitation.InviteActionResp
import com.doozez.doozez.api.invitation.InviteCreateReq
import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.framework.datasource.network.model.InvitationNetworkEntity
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface InvitationRetrofit {

    @GET("v1/invitations/")
    suspend fun getAll(): Response<PaginatedListResponse<InvitationNetworkEntity>>

//    @GET("v1/invitations/")
//    fun getInvitationsForSafe(@Query("safe") safeId: Int): Call<PaginatedListResponse<InviteDetailResp>>
//
//    @POST("v1/invitations/")
//    fun createInvitation(@Body body: InviteCreateReq): Call<Void>
//
//    @PATCH("v1/invitations/{id}/")
//    fun updateInvitationForAction(@Path("id") id: Int, @Body body: InviteActionReq): Call<InviteActionResp>
}