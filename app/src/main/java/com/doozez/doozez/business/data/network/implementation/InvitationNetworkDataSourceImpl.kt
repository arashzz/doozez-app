package com.doozez.doozez.business.data.network.implementation

import com.doozez.doozez.business.data.network.abstraction.InvitationNetworkDataSource
import com.doozez.doozez.business.domain.models.Invitation

class InvitationNetworkDataSourceImpl
constructor(
    private val retrofitService: InvitationRetrofitService,
    private val networkMapper: InvitationNetworkMapper
): InvitationNetworkDataSource {
    override suspend fun getAll(): List<Invitation> {
        return networkMapper.mapFromEntityList(retrofitService.getAll())
    }
}