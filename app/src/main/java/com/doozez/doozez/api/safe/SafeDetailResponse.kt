package com.doozez.doozez.api.safe

import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.google.gson.annotations.SerializedName

class SafeDetailResponse {

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("monthly_payment")
    var monthlyPayment: Long? = null

    @SerializedName("invitations")
    var invitations: List<InvitationDetailResponse> = ArrayList<InvitationDetailResponse>()
}