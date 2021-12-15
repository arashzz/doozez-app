package com.doozez.doozez.business.domain.models

import com.google.gson.annotations.SerializedName

data class InviteCreateReq (
    @SerializedName("safe")
    var safeId: Int,

    @SerializedName("recipient")
    var recipientId: Int
)