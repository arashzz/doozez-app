package com.doozez.doozez.di

import com.doozez.doozez.business.data.network.abstraction.InvitationNetworkDataSource
import com.doozez.doozez.business.interactors.invitation.GetInvitations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InteractorsModule {

    @Singleton
    @Provides
    fun provideGetInvitations(
        invitationNetworkDataSource: InvitationNetworkDataSource
    ): GetInvitations{
        return GetInvitations(invitationNetworkDataSource)
    }
}