package com.doozez.doozez.ui.user.listeners

import com.doozez.doozez.api.safe.SafeDetailResponse
import com.doozez.doozez.api.user.UserItemResponse

interface OnUserSearchItemClickListener {
    fun userItemClicked(item: UserItemResponse)
}