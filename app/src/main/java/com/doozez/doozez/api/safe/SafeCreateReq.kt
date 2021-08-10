package com.doozez.doozez.api.safe

import com.google.gson.annotations.SerializedName

class SafeCreateReq(

    @SerializedName("name")
    var name: String,

    @SerializedName("monthly_payment")
    var monthlyPayment: Int,

    @SerializedName("payment_method_id")
    var paymentMethodId: Int,
)