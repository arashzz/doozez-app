package com.doozez.doozez.api.user

import retrofit2.Call
import retrofit2.http.*

interface UserV1Service {

//    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @GET("v1/users/")
    fun searchUsers(@Query("email") email: String): Call<List<UserDetailResp>>

    @GET("v1/users/{id}/")
    fun getUserProfile(@Path("id") id: Int): Call<UserDetailResp>

    @PATCH("v1/users/{id}/")
    fun updateUser(@Path("id") id: Int, @Body body: UserProfileReq): Call<UserDetailResp>


}