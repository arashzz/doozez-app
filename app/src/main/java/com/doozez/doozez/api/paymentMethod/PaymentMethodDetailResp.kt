package com.doozez.doozez.api.paymentMethod

import com.google.gson.annotations.SerializedName

class PaymentMethodDetailResp(

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    //@SerializedName("type")
    var type: String = "DIRECT_DEBIT",

    @SerializedName("is_default")
    var isDefault: Boolean,

    @SerializedName("redirect_url")
    var redirectURL: String,

    @SerializedName("status")
    var status: String
)