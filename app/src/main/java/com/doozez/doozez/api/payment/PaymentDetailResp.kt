package com.doozez.doozez.api.payment

import com.doozez.doozez.api.participation.ParticipationResp
import com.google.gson.annotations.SerializedName
import java.util.*

class PaymentDetailResp (
    @SerializedName("id")
    var id: Int,

    @SerializedName("status")
    var status: String,

    @SerializedName("amount")
    var amount: Int,

    @SerializedName("charge_date")
    var chargeDate: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("participation")
    var participation: ParticipationResp
)