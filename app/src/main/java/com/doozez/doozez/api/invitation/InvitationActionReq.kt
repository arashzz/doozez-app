package com.doozez.doozez.api.invitation

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class InvitationActionReq(act: String, paymentId: Long) {

    @SerializedName("action")
    val action: String = act

    @SerializedName("json_data")
    var jsonData: String = JSONObject().put("payment_method_id", paymentId).toString()

}