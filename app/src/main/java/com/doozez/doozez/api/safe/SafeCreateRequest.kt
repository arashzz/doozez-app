package com.doozez.doozez.api.safe

import com.google.gson.annotations.SerializedName

class SafeCreateRequest {

    @SerializedName("userId")
    var userId: Long? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("monthlyPayment")
    var monthlyPayment: Long? = null
}