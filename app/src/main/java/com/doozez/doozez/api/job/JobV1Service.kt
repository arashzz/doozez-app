package com.doozez.doozez.api.job

import retrofit2.Call
import retrofit2.http.*

interface JobV1Service {
    @GET("v1/jobs/")
    fun getJobBySafe(@Query("safe") safeId: Int): Call<List<JobDetailResp>>
}