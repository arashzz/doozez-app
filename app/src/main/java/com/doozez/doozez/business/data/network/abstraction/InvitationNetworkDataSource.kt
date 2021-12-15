package com.doozez.doozez.business.data.network.abstraction

import com.doozez.doozez.business.domain.models.Invitation
import com.doozez.doozez.business.domain.models.InvitationReply

interface InvitationNetworkDataSource {
    suspend fun getAll(): List<Invitation>
    suspend fun reply(reply: InvitationReply)
}