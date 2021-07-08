package com.doozez.doozez.api.participation

import retrofit2.Call
import retrofit2.http.*

interface ParticipationV1Service {
    @GET("v1/participations/")
    fun getParticipationsForSafe(@Query("safe") safeId: Long): Call<List<ParticipationResp>>

    @GET("v1/participations/{id}")
    fun getParticipationByID(@Path("id") id: Long): Call<ParticipationResp>
}