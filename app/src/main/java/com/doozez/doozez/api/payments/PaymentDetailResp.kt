package com.doozez.doozez.api.payments

import com.google.gson.annotations.SerializedName

class PaymentDetailResp(

    @SerializedName("id")
    var id: Int,

    @SerializedName("card_number")
    var cardNumber: String = "1111 2222 3333 4444",

    @SerializedName("type")
    var type: String = "VISA",

    @SerializedName("is_default")
    var isDefault: Boolean = true,

    @SerializedName("redirect_url")
    var redirectURL: String
)