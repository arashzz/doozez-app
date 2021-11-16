package com.doozez.doozez.api

import com.google.gson.annotations.SerializedName

class PaginatedListResponse<T> (
    @SerializedName("results")
    var results: List<T>,

    @SerializedName("count")
    var count: Int
)