package com.doozez.doozez.api.participation

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class ParticipationActionReq(act: String) {

    @SerializedName("action")
    val action: String = act
}