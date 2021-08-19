package com.doozez.doozez.api.auth

import com.doozez.doozez.api.user.UserDetailResp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthV1Service {

    @POST("/auth/registration/")
    fun register(@Body body: RegisterCreateReq): Call<Void>

    @POST("/auth/login/")
    fun login(@Body body: LoginCreateReq): Call<LoginCreateResp>

    @GET("/v1/tokens/user/")
    fun validateToken(): Call<UserDetailResp>
}