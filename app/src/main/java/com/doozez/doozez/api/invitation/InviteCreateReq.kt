package com.doozez.doozez.api.invitation

import com.google.gson.annotations.SerializedName

class InviteCreateReq {
    @SerializedName("safe")
    var safeId: Int? = null

    @SerializedName("recipient")
    var recipientId: Int? = null
}