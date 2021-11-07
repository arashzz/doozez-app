package com.doozez.doozez.ui.user.listeners

import com.doozez.doozez.api.user.UserDetailResp

interface UserInviteeListener {
    fun inviteeRemoved(user: UserDetailResp)
}