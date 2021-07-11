package com.doozez.doozez.api.auth

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthV1Service {

    @POST("/auth/registration/")
    fun register(@Body body: RegisterCreateReq): Call<Void>
}