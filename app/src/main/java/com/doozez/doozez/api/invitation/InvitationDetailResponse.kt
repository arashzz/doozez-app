package com.doozez.doozez.api.invitation

import com.doozez.doozez.api.user.UserDetailResponse
import com.google.gson.annotations.SerializedName

class InvitationDetailResponse {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("recipient")
    var recipient: UserDetailResponse? = null

    @SerializedName("sender")
    var sender: UserDetailResponse? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("monthlyPayment")
    var monthlyPayment: Long? = null
}