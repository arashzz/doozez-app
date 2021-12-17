package com.doozez.doozez.api

import com.google.gson.annotations.SerializedName

data class PaginatedListResponse<T> (
    @SerializedName("results")
    var results: List<T> = ArrayList(),

    @SerializedName("count")
    var count: Int
)