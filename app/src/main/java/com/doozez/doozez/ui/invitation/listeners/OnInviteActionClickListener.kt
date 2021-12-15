package com.doozez.doozez.ui.invitation.listeners

import com.doozez.doozez.api.invitation.InviteDetailResp

interface OnInviteActionClickListener {
    fun inviteAccepted(invite: InviteDetailResp)
    fun inviteDeclined(invite: InviteDetailResp)
//    fun inviteCancelled(invite: InviteDetailResp)
    fun inviteSafeClicked(invite: InviteDetailResp)
}