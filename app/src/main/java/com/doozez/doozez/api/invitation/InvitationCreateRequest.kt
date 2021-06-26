package com.doozez.doozez.api.invitation

import com.google.gson.annotations.SerializedName

class InvitationCreateRequest {
    @SerializedName("safeId")
    var safeId: Long? = null

    @SerializedName("senderId")
    var senderId: Long? = null

    @SerializedName("recipientId")
    var recipientId: Long? = null
}