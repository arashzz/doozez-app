package com.doozez.doozez.api.safe

import com.google.gson.annotations.SerializedName

class SafeCreateRequest {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("monthly_payment")
    var monthlyPayment: Long? = null
}