package com.doozez.doozez.api.safe

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class SafeActionReq(act: String, force: Boolean) {

    @SerializedName("action")
    val action: String = act

    @SerializedName("json_data")
    var jsonData: String = JSONObject().put("force", force).toString()

}