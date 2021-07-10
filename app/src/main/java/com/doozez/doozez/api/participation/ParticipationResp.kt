package com.doozez.doozez.api.participation

import com.doozez.doozez.api.payments.PaymentDetailResp
import com.doozez.doozez.api.user.UserDetailResponse
import com.google.gson.annotations.SerializedName

class ParticipationResp() {

    @SerializedName("id")
    val id: Long? = null

    @SerializedName("user_role")
    var userRole: String? = null

    @SerializedName("user")
    var user: UserDetailResponse? = null

    @SerializedName("payment_method")
    var paymentMethod: PaymentDetailResp? = null

    @SerializedName("win_sequence")
    var winSequence: Long? = null
}