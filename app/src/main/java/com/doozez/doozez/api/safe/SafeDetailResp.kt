package com.doozez.doozez.api.safe

import android.os.Parcel
import android.os.Parcelable
import com.doozez.doozez.api.job.JobDetailResp
import com.google.gson.annotations.SerializedName

class SafeDetailResp(

    @SerializedName("id")
    var id: Int,

    @SerializedName("initiator")
    var initiator: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("status")
    var status: String,

    @SerializedName("monthly_payment")
    var monthlyPayment: Long
)