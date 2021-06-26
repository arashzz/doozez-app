package com.doozez.doozez.api.user

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserV1Service {

    @GET("v1/users")
    fun searchUsers(@Query("email") email: String): Call<List<UserItemResponse>>
}