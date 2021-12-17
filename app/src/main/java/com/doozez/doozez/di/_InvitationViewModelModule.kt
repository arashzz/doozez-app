//package com.doozez.doozez.di
//
//import android.content.SharedPreferences
//import androidx.lifecycle.ViewModelProvider
//import com.doozez.doozez.business.domain.models.invitation.InvitationFactory
//import com.doozez.doozez.business.interactors.invitation.InvitationListInteractors
//import com.doozez.doozez.framework.presentation.common.InvitationViewModelFactory
//import com.google.android.datatransport.runtime.dagger.Module
//import com.google.android.datatransport.runtime.dagger.Provides
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.FlowPreview
//import javax.inject.Singleton
//
//@ExperimentalCoroutinesApi
//@FlowPreview
//@Module
//object _InvitationViewModelModule {
//
//    @Singleton
//    @JvmStatic
//    @Provides
//    fun provideInvitationViewModelFactory(
//        invitationListInteractors: InvitationListInteractors,
//        //noteNetworkSyncManager: NoteNetworkSyncManager,
//        invitationFactory: InvitationFactory,
//        editor: SharedPreferences.Editor,
//        sharedPreferences: SharedPreferences
//    ): ViewModelProvider.Factory{
//        return InvitationViewModelFactory(
//            invitationListInteractors = invitationListInteractors,
////            noteDetailInteractors = noteDetailInteractors,
////            noteNetworkSyncManager = noteNetworkSyncManager,
//            factory = invitationFactory,
//            editor = editor,
//            sharedPreferences = sharedPreferences
//        )
//    }
//}