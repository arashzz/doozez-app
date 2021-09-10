package com.doozez.doozez.api.participation

import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp
import com.doozez.doozez.api.user.UserDetailResp
import com.google.gson.annotations.SerializedName

class ParticipationResp(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_role")
    var userRole: String,

    @SerializedName("user")
    var user: UserDetailResp,

    @SerializedName("payment_method")
    var paymentMethod: PaymentMethodDetailResp,

    @SerializedName("win_sequence")
    var winSequence: Int
)