package com.doozez.doozez.ui.user.listeners

import com.doozez.doozez.api.user.UserDetailResp

interface OnUserSearchItemClickListener {
    fun userItemClicked(user: UserDetailResp)
    fun selectedUserRemoved(user: UserDetailResp)
}