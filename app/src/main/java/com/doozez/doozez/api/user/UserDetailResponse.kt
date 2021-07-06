package com.doozez.doozez.api.user

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class UserDetailResponse() : Parcelable {
    @SerializedName("id")
    var id: Long = 0

    @SerializedName("email")
    var email: String = ""

    @SerializedName("first_name")
    var firstName: String = ""

    @SerializedName("last_name")
    var lastName: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        email = parcel.readString().toString()
        firstName = parcel.readString().toString()
        lastName = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDetailResponse> {
        override fun createFromParcel(parcel: Parcel): UserDetailResponse {
            return UserDetailResponse(parcel)
        }

        override fun newArray(size: Int): Array<UserDetailResponse?> {
            return arrayOfNulls(size)
        }
    }

}