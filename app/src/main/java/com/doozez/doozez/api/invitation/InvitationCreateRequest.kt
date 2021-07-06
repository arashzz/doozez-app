package com.doozez.doozez.api.invitation

import com.google.gson.annotations.SerializedName

class InvitationCreateRequest {
    @SerializedName("safe")
    var safeId: Long? = null

    @SerializedName("recipient")
    var recipientId: Long? = null
}