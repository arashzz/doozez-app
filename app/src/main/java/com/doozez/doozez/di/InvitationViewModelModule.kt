package com.doozez.doozez.di

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.doozez.doozez.business.domain.models.InvitationFactory
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Module
object InvitationViewModelModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideInvitationViewModelFactory(
        invitationListInteractors: InvitationListInteractors,
        //noteNetworkSyncManager: NoteNetworkSyncManager,
        invitationFactory: InvitationFactory,
        editor: SharedPreferences.Editor,
        sharedPreferences: SharedPreferences
    ): ViewModelProvider.Factory{
        return NoteViewModelFactory(
            noteListInteractors = noteListInteractors,
            noteDetailInteractors = noteDetailInteractors,
            noteNetworkSyncManager = noteNetworkSyncManager,
            noteFactory = noteFactory,
            editor = editor,
            sharedPreferences = sharedPreferences
        )
    }
}