package com.doozez.doozez.api.invitation

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface InvitationV1Service {
    @GET("v1/invitations")
    fun getInvitationsForUser(@Query("userId") userId: String): Call<List<InvitationDetailResponse>>

    @POST("v1/invitations")
    fun createInvitationForUser(@Body body: InvitationCreateRequest): Call<Void>
}