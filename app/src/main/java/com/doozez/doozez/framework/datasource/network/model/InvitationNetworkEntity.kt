package com.doozez.doozez.framework.datasource.network.model

import com.doozez.doozez.api.safe.SafeDetailResp
import com.doozez.doozez.api.user.UserDetailResp
import com.google.gson.annotations.SerializedName

data class InvitationNetworkEntity (
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