package com.doozez.doozez.business.domain.models

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class InvitationReply(

    @SerializedName("action")
    val action: String,

    @SerializedName("json_data")
    var jsonData: String,
)