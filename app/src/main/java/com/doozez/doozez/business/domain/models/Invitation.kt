package com.doozez.doozez.business.domain.models

import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.enums.InvitationStatus
import com.google.gson.annotations.SerializedName

data class Invitation (
    @SerializedName("id")
    var id: Int,

    @SerializedName("recipient")
    var recipient: UserDetailResp,

    @SerializedName("sender")
    var sender: UserDetailResp,

    @SerializedName("status")
    var status: String,

    @SerializedName("monthlyPayment")
    var monthlyPayment: Long,

    @SerializedName("safe")
    var safe: SafeDetailResp

)