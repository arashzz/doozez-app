package com.doozez.doozez.framework.presentation.invitation

import com.doozez.doozez.api.invitation.InviteDetailResp
import com.doozez.doozez.business.domain.models.invitation.Invitation

interface OnInviteActionClickListener {
    fun inviteAccepted(invite: Invitation)
    fun inviteDeclined(invite: Invitation)
//    fun inviteCancelled(invite: InviteDetailResp)
    fun inviteSafeClicked(invite: Invitation)
}