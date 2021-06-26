package com.doozez.doozez.ui.user.listeners

import com.doozez.doozez.api.user.UserDetailResponse

interface OnUserSearchItemClickListener {
    fun userItemClicked(detail: UserDetailResponse)
}