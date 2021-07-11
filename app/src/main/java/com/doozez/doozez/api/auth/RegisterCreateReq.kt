package com.doozez.doozez.api.auth

import com.google.gson.annotations.SerializedName

class RegisterCreateReq(
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("password1")
    val password: String,
    @SerializedName("password2")
    val passwordConfig: String
)