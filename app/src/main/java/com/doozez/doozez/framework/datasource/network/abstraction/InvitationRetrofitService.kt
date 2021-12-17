package com.doozez.doozez.framework.datasource.network.abstraction

import com.doozez.doozez.api.PaginatedListResponse
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.business.domain.models.invitation.InvitationReply
import com.doozez.doozez.framework.datasource.network.model.InvitationNetworkEntity

interface InvitationRetrofitService {
//    suspend fun create(invitation: Invitation)
    suspend fun getAll(): PaginatedListResponse<InvitationNetworkEntity>
//    suspend fun reply(invitationReply: InvitationReply)
}