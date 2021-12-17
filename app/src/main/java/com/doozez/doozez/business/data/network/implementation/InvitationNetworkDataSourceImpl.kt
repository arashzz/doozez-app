package com.doozez.doozez.business.data.network.implementation

import com.doozez.doozez.api.PaginatedListResponse
import com.doozez.doozez.business.data.network.abstraction.InvitationNetworkDataSource
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.business.domain.models.invitation.InvitationReply
import com.doozez.doozez.framework.datasource.network.abstraction.InvitationRetrofitService
import com.doozez.doozez.framework.datasource.network.mappers.InvitationNetworkMapper

class InvitationNetworkDataSourceImpl
constructor(
    private val retrofitService: InvitationRetrofitService,
    private val mapper: InvitationNetworkMapper
): InvitationNetworkDataSource {

    override suspend fun getAll(): PaginatedListResponse<Invitation> {
        val res = retrofitService.getAll()
        return mapper.mapFromEntityList(res)
    }

    override suspend fun reply(reply: InvitationReply) {
        TODO("Not yet implemented")
    }
}