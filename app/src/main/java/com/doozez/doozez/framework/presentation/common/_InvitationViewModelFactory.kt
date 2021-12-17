//package com.doozez.doozez.framework.presentation.common
//
//import android.content.SharedPreferences
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.doozez.doozez.business.domain.models.invitation.InvitationFactory
//import com.doozez.doozez.business.interactors.invitation.InvitationListInteractors
//import com.doozez.doozez.framework.presentation.invitationlist._InvitationListViewModel
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.FlowPreview
//
//@FlowPreview
//@ExperimentalCoroutinesApi
//class _InvitationViewModelFactory
//constructor(
//    private val invitationListInteractors: InvitationListInteractors,
////    private val noteDetailInteractors: NoteDetailInteractors,
////    private val noteNetworkSyncManager: NoteNetworkSyncManager,
//    private val factory: InvitationFactory,
//    private val editor: SharedPreferences.Editor,
//    private val sharedPreferences: SharedPreferences
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when(modelClass){
//
//            _InvitationListViewModel::class.java -> {
//                _InvitationListViewModel(
//                    listInteractors = invitationListInteractors,
//                    factory = factory,
//                    editor = editor,
//                    sharedPreferences = sharedPreferences
//                ) as T
//            }
//
////            DetailViewModel::class.java -> {
////                NoteDetailViewModel(
////                    noteInteractors = noteDetailInteractors
////                ) as T
////            }
//
////            SplashViewModel::class.java -> {
////                SplashViewModel(
////                    noteNetworkSyncManager = noteNetworkSyncManager
////                ) as T
////            }
//
//            else -> {
//                throw IllegalArgumentException("unknown model class $modelClass")
//            }
//        }
//    }
//}