package com.doozez.doozez.framework.datasource.network.mappers

import com.doozez.doozez.api.PaginatedListResponse
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.business.domain.util.DateUtil
import com.doozez.doozez.business.domain.util.EntityMapper
import com.doozez.doozez.enums.InvitationStatus
import com.doozez.doozez.framework.datasource.network.model.InvitationNetworkEntity
import javax.inject.Inject

class InvitationNetworkMapper
@Inject
constructor(
    private val dateUtil: DateUtil
): EntityMapper<InvitationNetworkEntity, Invitation>
{
    override fun mapFromEntity(entity: InvitationNetworkEntity): Invitation {
        return Invitation(
            id = entity.id,
            recipient = entity.recipient,
            sender = entity.sender,
            status = InvitationStatus.fromCode(entity.status),
            monthlyPayment = entity.monthlyPayment,
            safe = entity.safe
        )
    }

    override fun mapToEntity(domainModel: Invitation): InvitationNetworkEntity {
        return InvitationNetworkEntity(
            id = domainModel.id,
            recipient = domainModel.recipient,
            sender = domainModel.sender,
            status = domainModel.status.code,
            monthlyPayment = domainModel.monthlyPayment,
            safe = domainModel.safe
        )
    }


    fun mapFromEntityList(entities: PaginatedListResponse<InvitationNetworkEntity>): PaginatedListResponse<Invitation>{
        val results =  entities.results.map { mapFromEntity(it) }
        return PaginatedListResponse<Invitation>(results, entities.count)
    }
}