package com.doozez.doozez.api.safe

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class SafeDetailResponse() : Parcelable {

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("initiator")
    var initiator: Long? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("monthly_payment")
    var monthlyPayment: Long? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
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

    companion object CREATOR : Parcelable.Creator<SafeDetailResponse> {
        override fun createFromParcel(parcel: Parcel): SafeDetailResponse {
            return SafeDetailResponse(parcel)
        }

        override fun newArray(size: Int): Array<SafeDetailResponse?> {
            return arrayOfNulls(size)
        }
    }
}