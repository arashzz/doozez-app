package com.doozez.doozez.api.participation

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ParticipationV1Service {
    @GET("v1/participations/")
    fun getParticipationsForSafe(@Query("safe") safeId: Long): Call<List<ParticipationResp>>

    @GET("v1/participations/{id}")
    fun getParticipationByID(@Path("id") id: Long): Call<ParticipationResp>
}