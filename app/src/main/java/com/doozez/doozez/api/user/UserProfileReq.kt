package com.doozez.doozez.api.user

import com.google.gson.annotations.SerializedName

class UserProfileReq(
    @SerializedName("first_name")
    var firstName: String,

    @SerializedName("last_name")
    var lastName: String
)