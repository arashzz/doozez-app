//package com.doozez.doozez.framework.presentation.invitationlist.state
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.doozez.doozez.business.domain.models.invitation.Invitation
//import com.doozez.doozez.framework.presentation.invitationlist.state.InvitationListToolbarState.*
//
//class InvitationListInteractionManager {
//
//    private val _selectedInvitations: MutableLiveData<ArrayList<Invitation>> = MutableLiveData()
//
//    private val _toolbarState: MutableLiveData<InvitationListToolbarState>
//            = MutableLiveData(SearchViewState())
//
//    val selectedInvitations: LiveData<ArrayList<Invitation>>
//        get() = _selectedInvitations
//
//    val toolbarState: LiveData<InvitationListToolbarState>
//        get() = _toolbarState
//
//    fun setToolbarState(state: InvitationListToolbarState){
//        _toolbarState.value = state
//    }
//
//    fun getSelectedInvitations():ArrayList<Invitation> = _selectedInvitations.value?: ArrayList()
//
//    fun isMultiSelectionStateActive(): Boolean{
//        return _toolbarState.value.toString() == MultiSelectionState().toString()
//    }
//
//    fun addOrRemoveInvitationFromSelectedList(invitation: Invitation){
//        var list = _selectedInvitations.value
//        if(list == null){
//            list = ArrayList()
//        }
//        if (list.contains(invitation)){
//            list.remove(invitation)
//        }
//        else{
//            list.add(invitation)
//        }
//        _selectedInvitations.value = list
//    }
//
//    fun isInvitationSelected(invitation: Invitation): Boolean{
//        return _selectedInvitations.value?.contains(invitation)?: false
//    }
//
//    fun clearSelectedInvitations(){
//        _selectedInvitations.value = null
//    }
//}