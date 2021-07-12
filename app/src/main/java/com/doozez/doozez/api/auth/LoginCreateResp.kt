package com.doozez.doozez.api.auth

import com.google.gson.annotations.SerializedName

class LoginCreateResp(
    @SerializedName("key")
    val apiKey: String
)