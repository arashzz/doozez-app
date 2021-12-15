package com.doozez.doozez.business.interactors.invitation

import com.doozez.doozez.business.data.network.abstraction.InvitationNetworkDataSource
import com.doozez.doozez.business.data.util.safeApiCall
import com.doozez.doozez.business.domain.models.InvitationFactory
import com.doozez.doozez.business.domain.models.InvitationReply
import com.doozez.doozez.business.domain.state.DataState
import com.doozez.doozez.enums.InvitationAction
import com.doozez.doozez.framework.presentation.invitationlist.state.InvitationListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReplyInvitation (
    private val networkDataSource: InvitationNetworkDataSource,
    private val factory: InvitationFactory
){

    fun replyInvitation(
        action: InvitationAction,
        paymentMethodID: Int
    ): Flow<DataState<InvitationListViewState>?> = flow {
        val reply = factory.createReply(action, paymentMethodID)
        updateNetwork(reply)
    }

    private suspend fun updateNetwork(reply: InvitationReply ){

        safeApiCall(IO){
            networkDataSource.reply(reply)
        }
    }

    companion object{
        val REPLY_INVITATION_SUCCESS = "Successfully inserted new note."
        val REPLY_INVITATION_FAILED = "Failed to insert new note."
    }
}