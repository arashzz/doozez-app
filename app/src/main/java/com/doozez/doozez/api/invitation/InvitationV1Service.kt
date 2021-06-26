package com.doozez.doozez.api.invitation

import retrofit2.Call
import retrofit2.http.*

interface InvitationV1Service {
    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @GET("v1/invitations")
    fun getInvitationsForUser(@Query("userId") userId: String): Call<List<InvitationDetailResponse>>

    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @POST("v1/invitations")
    fun createInvitationForUser(@Body body: InvitationCreateRequest): Call<Void>
}