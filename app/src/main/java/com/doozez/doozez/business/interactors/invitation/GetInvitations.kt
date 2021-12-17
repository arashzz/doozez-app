package com.doozez.doozez.business.interactors.invitation

import com.doozez.doozez.api.PaginatedListResponse
import com.doozez.doozez.business.data.network.abstraction.InvitationNetworkDataSource
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.business.domain.state.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetInvitations
constructor(
    private val networkDataSource: InvitationNetworkDataSource
){

    private val TAG: String = "AppDebug"

    /**
     * Show loading
     * Get blogs from network
     * Insert blogs into cache
     * Show List<Blog>
     */
    suspend fun execute(): Flow<DataState<PaginatedListResponse<Invitation>>> = flow {
        emit(DataState.Loading)
        delay(1000)
        val networkBlogs = networkDataSource.getAll()
        emit(DataState.Success(networkBlogs))
    }

}