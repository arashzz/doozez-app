package com.doozez.doozez.api.device

import com.doozez.doozez.api.user.UserDetailResp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DeviceV1Service {

    @POST("/devices")
    fun register(@Body body: DeviceCreateReq): Call<Void>

}