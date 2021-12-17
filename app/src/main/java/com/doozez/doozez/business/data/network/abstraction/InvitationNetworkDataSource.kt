package com.doozez.doozez.business.data.network.abstraction

import com.doozez.doozez.api.PaginatedListResponse
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.business.domain.models.invitation.InvitationReply

interface InvitationNetworkDataSource {
    suspend fun getAll(): PaginatedListResponse<Invitation>
    suspend fun reply(reply: InvitationReply)
}