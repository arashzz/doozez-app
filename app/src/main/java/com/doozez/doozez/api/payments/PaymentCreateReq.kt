package com.doozez.doozez.api.payments

import com.google.gson.annotations.SerializedName

class PaymentCreateReq(
    @SerializedName("is_default")
    var isDefault: Boolean
)