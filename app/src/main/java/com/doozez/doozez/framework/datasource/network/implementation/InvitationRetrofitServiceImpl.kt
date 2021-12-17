package com.doozez.doozez.framework.datasource.network.implementation

import com.doozez.doozez.api.PaginatedListResponse
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.business.domain.models.invitation.InvitationReply
import com.doozez.doozez.framework.datasource.network.abstraction.InvitationRetrofitService
import com.doozez.doozez.framework.datasource.network.mappers.InvitationNetworkMapper
import com.doozez.doozez.framework.datasource.network.model.InvitationNetworkEntity
import com.doozez.doozez.framework.datasource.network.retrofit.InvitationRetrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvitationRetrofitServiceImpl
@Inject
constructor(
    private val retrofit: InvitationRetrofit
): InvitationRetrofitService {

//    override suspend fun create(invitation: Invitation) {
//        TODO("Not yet implemented")
//    }

    override suspend fun getAll(): PaginatedListResponse<InvitationNetworkEntity> {
        val response = retrofit.getAll()
        if(response.isSuccessful && response.body() != null) {
            val count = response.body()!!.count
            val num = count
        } else {
            val s = "123123123"
        }
        return PaginatedListResponse(ArrayList(),1)
    }

//    override suspend fun reply(invitationReply: InvitationReply) {
//        TODO("Not yet implemented")
//    }

}