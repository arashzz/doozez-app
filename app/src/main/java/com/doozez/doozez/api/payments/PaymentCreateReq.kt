package com.doozez.doozez.api.payments

import com.google.gson.annotations.SerializedName

class PaymentCreateReq(

    @SerializedName("name")
    var name: String,

    @SerializedName("is_default")
    var isDefault: Boolean
)