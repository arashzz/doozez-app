package com.doozez.doozez.api.invitation

import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.utils.InvitationStatus
import com.google.gson.annotations.SerializedName

class InviteDetailResp (
    @SerializedName("id")
    var id: Int,

    @SerializedName("recipient")
    var recipient: UserDetailResp,

//    @SerializedName("initiator")
//    var initiator: UserDetailResp,

    @SerializedName("status")
    var status: String,

    @SerializedName("monthlyPayment")
    var monthlyPayment: Long,

    @SerializedName("safe")
    var safe: SafeDetailResp

        ): Comparable<InviteDetailResp> {
    override fun compareTo(other: InviteDetailResp): Int {
        if (status == other.status) {
            return 0
        } else if (status != InvitationStatus.PENDING && other.status == InvitationStatus.PENDING) {
            return 1
        }
        return -1
    }

}