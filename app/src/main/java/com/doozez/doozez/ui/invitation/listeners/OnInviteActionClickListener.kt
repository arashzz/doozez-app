package com.doozez.doozez.ui.invitation.listeners

import com.doozez.doozez.api.invitation.InvitationDetailResponse

interface OnInviteActionClickListener {
    fun inviteAccepted(invite: InvitationDetailResponse)
    fun inviteDeclined(invite: InvitationDetailResponse)
    fun inviteSafeClicked(invite: InvitationDetailResponse)
}