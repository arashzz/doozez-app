package com.doozez.doozez.ui.safe.listeners

import com.doozez.doozez.api.invitation.InviteDetailResp

interface SafeInviteeListener {
    fun inviteeRemoved(invitation: InviteDetailResp)
}