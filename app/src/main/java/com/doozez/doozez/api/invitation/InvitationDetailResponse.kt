package com.doozez.doozez.api.invitation

import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.api.user.UserDetailResponse
import com.doozez.doozez.utils.InvitationStatus
import com.google.gson.annotations.SerializedName

class InvitationDetailResponse (
    @SerializedName("id")
    var id: Int,

    @SerializedName("recipient")
    var recipient: UserDetailResponse,

    @SerializedName("initiator")
    var initiator: UserDetailResponse,

    @SerializedName("status")
    var status: String,

    @SerializedName("monthlyPayment")
    var monthlyPayment: Long,

    @SerializedName("safe")
    var safe: SafeDetailResponse

        ): Comparable<InvitationDetailResponse> {
    override fun compareTo(other: InvitationDetailResponse): Int {
        if (status == other.status) {
            return 0
        } else if (status != InvitationStatus.PENDING && other.status == InvitationStatus.PENDING) {
            return 1
        }
        return -1
    }

}