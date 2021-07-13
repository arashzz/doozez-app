package com.doozez.doozez.api.participation

import com.doozez.doozez.api.payments.PaymentDetailResp
import com.doozez.doozez.api.user.UserDetailResponse
import com.google.gson.annotations.SerializedName

class ParticipationResp(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_role")
    var userRole: String,

    @SerializedName("user")
    var user: UserDetailResponse,

    @SerializedName("payment_method")
    var paymentMethod: PaymentDetailResp,

    @SerializedName("win_sequence")
    var winSequence: Int
)