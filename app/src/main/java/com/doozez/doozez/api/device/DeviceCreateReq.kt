package com.doozez.doozez.api.device

import com.google.gson.annotations.SerializedName

class DeviceCreateReq(
    @SerializedName("registration_id")
    val registrationID: String,
    @SerializedName("user")
    val userID: Int,
    @SerializedName("type")
    val type: String = "android",
)