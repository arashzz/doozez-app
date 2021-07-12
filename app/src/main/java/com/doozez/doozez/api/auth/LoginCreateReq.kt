package com.doozez.doozez.api.auth

import com.google.gson.annotations.SerializedName

class LoginCreateReq(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)