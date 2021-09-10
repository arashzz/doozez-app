package com.doozez.doozez.api.job

import retrofit2.Call
import retrofit2.http.*

interface JobV1Service {
    @GET("v1/jobs/{id}")
    fun getJobByID(@Query("id") jobId: Int): Call<List<Void>>

}