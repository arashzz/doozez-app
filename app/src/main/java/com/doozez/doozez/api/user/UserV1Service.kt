package com.doozez.doozez.api.user

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserV1Service {

//    @Headers("Authorization: Basic YWRtaW5AZG9vemV6LmNvbTpqZXJtaW4xMjNA")
    @GET("v1/users/")
    fun searchUsers(@Query("email") email: String): Call<List<UserDetailResponse>>


}