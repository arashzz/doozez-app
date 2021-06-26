package com.doozez.doozez.api.user

import com.google.gson.annotations.SerializedName

class UserDetailResponse {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null
}