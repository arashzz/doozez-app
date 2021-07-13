package com.doozez.doozez.api.user

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class UserDetailResponse(
    @SerializedName("id")
    var id: Int,

    @SerializedName("email")
    var email: String,

    @SerializedName("first_name")
    var firstName: String,

    @SerializedName("last_name")
    var lastName: String

)