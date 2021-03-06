package com.doozez.doozez.api.participation

import retrofit2.Call
import retrofit2.http.*

interface ParticipationV1Service {
    @GET("v1/participations/")
    fun getParticipationsForSafe(@Query("safe") safeId: Int): Call<List<ParticipationResp>>

    @GET("v1/participations/{id}/")
    fun getParticipationByID(@Path("id") id: Int): Call<ParticipationResp>

    @PATCH("v1/participations/{id}/")
    fun updateParticipationForAction(@Path("id") id: Int, @Body body: ParticipationActionReq): Call<Void>
}