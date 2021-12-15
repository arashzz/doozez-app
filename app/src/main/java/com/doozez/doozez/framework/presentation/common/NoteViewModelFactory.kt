package com.doozez.doozez.framework.presentation.common

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doozez.doozez.business.domain.models.InvitationFactory
import com.doozez.doozez.business.interactors.invitation.InvitationListInteractors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class NoteViewModelFactory
constructor(
    private val invitationListInteractors: InvitationListInteractors,
//    private val noteDetailInteractors: NoteDetailInteractors,
//    private val noteNetworkSyncManager: NoteNetworkSyncManager,
    private val factory: InvitationFactory,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass){

            InvitationListViewModel::class.java -> {
                NoteListViewModel(
                    noteInteractors = noteListInteractors,
                    noteFactory = noteFactory,
                    editor = editor,
                    sharedPreferences = sharedPreferences
                ) as T
            }

            NoteDetailViewModel::class.java -> {
                NoteDetailViewModel(
                    noteInteractors = noteDetailInteractors
                ) as T
            }

            SplashViewModel::class.java -> {
                SplashViewModel(
                    noteNetworkSyncManager = noteNetworkSyncManager
                ) as T
            }

            else -> {
                throw IllegalArgumentException("unknown model class $modelClass")
            }
        }
    }
}