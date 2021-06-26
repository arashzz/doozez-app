package com.doozez.doozez.api.invitation

import com.google.gson.annotations.SerializedName

class InvitationDetailResponse {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("recipient")
    var recipient: String? = null

    @SerializedName("sender")
    var sender: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("monthlyPayment")
    var monthlyPayment: Long? = null
}