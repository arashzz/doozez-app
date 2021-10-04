package com.doozez.doozez.api.safe

import android.os.Parcel
import android.os.Parcelable
import com.doozez.doozez.api.job.JobDetailResp
import com.google.gson.annotations.SerializedName

class SafeDetailResp() : Parcelable {

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("initiator")
    var initiator: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("monthly_payment")
    var monthlyPayment: Long? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
        status = parcel.readString()
        monthlyPayment = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(status)
        parcel.writeValue(monthlyPayment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SafeDetailResp> {
        override fun createFromParcel(parcel: Parcel): SafeDetailResp {
            return SafeDetailResp(parcel)
        }

        override fun newArray(size: Int): Array<SafeDetailResp?> {
            return arrayOfNulls(size)
        }
    }
}