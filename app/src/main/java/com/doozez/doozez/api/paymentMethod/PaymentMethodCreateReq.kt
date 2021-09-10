package com.doozez.doozez.api.paymentMethod

import com.google.gson.annotations.SerializedName

class PaymentMethodCreateReq(

    @SerializedName("name")
    var name: String,

    @SerializedName("is_default")
    var isDefault: Boolean
)