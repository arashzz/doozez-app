//package com.doozez.doozez.framework.presentation.invitationlist.state
//
//import android.os.Parcelable
//import com.doozez.doozez.business.domain.models.invitation.Invitation
//import com.doozez.doozez.business.domain.state.ViewState
//import kotlinx.android.parcel.Parcelize
//
//@Parcelize
//data class _InvitationListViewState(
//
//    var invitationList: ArrayList<Invitation>? = null,
//    var newInvitation: Invitation? = null, // note that can be created with fab
//    var searchQuery: String? = null,
//    var page: Int? = null,
//    var isQueryExhausted: Boolean? = null,
//    var filter: String? = null,
//    var order: String? = null,
//    var layoutManagerState: Parcelable? = null,
//
//    ) : Parcelable, ViewState {
//
//    @Parcelize
//    data class NotePendingDelete(
//        var invitation: Invitation? = null,
//        var listPosition: Int? = null
//    ) : Parcelable
//}