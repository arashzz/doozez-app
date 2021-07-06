package com.doozez.doozez.api.invitation

import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.api.user.UserDetailResponse
import com.doozez.doozez.utils.InvitationStatus
import com.google.gson.annotations.SerializedName

class InvitationDetailResponse: Comparable<InvitationDetailResponse> {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("recipient")
    var recipient: UserDetailResponse? = null

    @SerializedName("initiator")
    var initiator: UserDetailResponse? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("monthlyPayment")
    var monthlyPayment: Long? = null

    @SerializedName("safe")
    var safe: SafeDetailResponse? = null
    override fun compareTo(other: InvitationDetailResponse): Int {
        if (status == other.status) {
            return 0
        } else if (status != InvitationStatus.PENDING && other.status == InvitationStatus.PENDING) {
            return 1
        }
        return -1
    }

}