package com.doozez.doozez.business.domain.models.invitation

import com.google.gson.annotations.SerializedName

data class InvitationReq (
    @SerializedName("safe")
    var safeId: Int,

    @SerializedName("recipient")
    var recipientId: Int
)