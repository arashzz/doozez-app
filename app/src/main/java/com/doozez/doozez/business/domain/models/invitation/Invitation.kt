package com.doozez.doozez.business.domain.models.invitation

import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.enums.InvitationStatus
import com.google.gson.annotations.SerializedName

data class Invitation (
    var id: Int,

    var recipient: UserDetailResp,

    var sender: UserDetailResp,

    var status: InvitationStatus,

    var monthlyPayment: Long,

    var safe: SafeDetailResp
)