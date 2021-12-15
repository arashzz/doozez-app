package com.doozez.doozez.business.interactors.invitation

import com.doozez.doozez.business.data.network.ApiResponseHandler
import com.doozez.doozez.business.data.network.abstraction.InvitationNetworkDataSource
import com.doozez.doozez.business.data.util.safeApiCall
import com.doozez.doozez.business.domain.models.Invitation
import com.doozez.doozez.business.domain.state.*
import com.doozez.doozez.framework.presentation.invitationlist.state.InvitationListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchInvitations (
    private val networkDataSource: InvitationNetworkDataSource
){
    fun searchInvitations(
        stateEvent: StateEvent
    ): Flow<DataState<InvitationListViewState>?> = flow {
        val networkResult = safeApiCall(IO) {
            networkDataSource.getAll()
        }

        val response = object: ApiResponseHandler<InvitationListViewState, List<Invitation>>(
            response = networkResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<Invitation>): DataState<InvitationListViewState>? {
                var message: String? =
                    SEARCH_INVITATIONS_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None()
                if(resultObj.isEmpty()){
                    message =
                        SEARCH_INVITATIONS_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType as UIComponentType,
                        messageType = MessageType.Success()
                    ),
                    data = InvitationListViewState(
                        invitationList = ArrayList(resultObj)
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }
    companion object{
        val SEARCH_INVITATIONS_SUCCESS = "Successfully retrieved list of invitations."
        val SEARCH_INVITATIONS_NO_MATCHING_RESULTS = "There are no invitations that match that query."
        val SEARCH_INVITATIONS_FAILED = "Failed to retrieve the list of invitations."

    }
}