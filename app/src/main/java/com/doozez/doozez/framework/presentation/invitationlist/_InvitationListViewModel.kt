//package com.doozez.doozez.framework.presentation.invitationlist
//
//import android.content.SharedPreferences
//import android.os.Parcelable
//import androidx.lifecycle.LiveData
//import com.doozez.doozez.business.domain.models.invitation.Invitation
//import com.doozez.doozez.business.domain.models.invitation.InvitationFactory
//import com.doozez.doozez.business.domain.state.DataState
//import com.doozez.doozez.business.domain.state.StateEvent
//import com.doozez.doozez.business.interactors.invitation.InvitationListInteractors
//import com.doozez.doozez.framework.datasource.preferences.PreferenceKeys.Companion.NOTE_FILTER
//import com.doozez.doozez.framework.datasource.preferences.PreferenceKeys.Companion.NOTE_ORDER
//import com.doozez.doozez.framework.presentation.common._BaseViewModel
//import com.doozez.doozez.framework.presentation.invitationlist.state.InvitationListInteractionManager
//import com.doozez.doozez.framework.presentation.invitationlist.state.InvitationListToolbarState
//import com.doozez.doozez.framework.presentation.invitationlist.state._InvitationListViewState
//import com.doozez.doozez.framework.presentation.invitationlist.state.InvitationListStateEvent.*
//import com.doozez.doozez.utils.printLogD
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.FlowPreview
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//import javax.inject.Singleton
//
////const val DELETE_PENDING_ERROR = "There is already a pending delete operation."
////const val NOTE_PENDING_DELETE_BUNDLE_KEY = "pending_delete"
//
//@ExperimentalCoroutinesApi
//@FlowPreview
//@Singleton
//class _InvitationListViewModel
//@Inject
//constructor(
//    private val listInteractors: InvitationListInteractors,
//    private val factory: InvitationFactory,
//    private val editor: SharedPreferences.Editor,
//    sharedPreferences: SharedPreferences
//): _BaseViewModel<_InvitationListViewState>(){
//
//    val invitationListInteractionManager =
//        InvitationListInteractionManager()
//
//    val toolbarState: LiveData<InvitationListToolbarState>
//        get() = invitationListInteractionManager.toolbarState
//
//    init {
////        setInvitationFilter(
////            sharedPreferences.getString(
////                NOTE_FILTER,
////                NOTE_FILTER_DATE_CREATED
////            )
////        )
////        setInvitationOrder(
////            sharedPreferences.getString(
////                NOTE_ORDER,
////                NOTE_ORDER_DESC
////            )
////        )
//    }
//
//    override fun handleNewData(data: _InvitationListViewState) {
//
//        data.let { viewState ->
//            viewState.invitationList?.let { invitationList ->
//                setInvitationListData(invitationList)
//            }
//
////            viewState.numInvitationsInCache?.let { numInvitations ->
////                setNumInvitationsInCache(numInvitations)
////            }
//
//            viewState.newInvitation?.let { invitation ->
//                setInvitation(invitation)
//            }
//
////            viewState.invitationPendingDelete?.let { restoredInvitation ->
////                restoredInvitation.invitation?.let { invitation ->
////                    setRestoredInvitationId(invitation)
////                }
////                setInvitationPendingDelete(null)
////            }
//        }
//
//    }
//
//    override fun setStateEvent(stateEvent: StateEvent) {
//
//        val job: Flow<DataState<_InvitationListViewState>?> = when(stateEvent){
//
////            is InsertNewInvitationEvent -> {
////                invitationInteractors.insertNewInvitation.insertNewInvitation(
////                    title = stateEvent.title,
////                    stateEvent = stateEvent
////                )
////            }
//
////            is InsertMultipleInvitationsEvent -> {
////                invitationInteractors.insertMultipleInvitations.insertInvitations(
////                    numInvitations = stateEvent.numInvitations,
////                    stateEvent = stateEvent
////                )
////            }
//
////            is DeleteInvitationEvent -> {
////                invitationInteractors.deleteInvitation.deleteInvitation(
////                    invitation = stateEvent.invitation,
////                    stateEvent = stateEvent
////                )
////            }
//
////            is DeleteMultipleInvitationsEvent -> {
////                invitationInteractors.deleteMultipleInvitations.deleteInvitations(
////                    invitations = stateEvent.invitations,
////                    stateEvent = stateEvent
////                )
////            }
////
////            is RestoreDeletedInvitationEvent -> {
////                invitationInteractors.restoreDeletedInvitation.restoreDeletedInvitation(
////                    invitation = stateEvent.invitation,
////                    stateEvent = stateEvent
////                )
////            }
//
//            is SearchInvitationsEvent -> {
//                if(stateEvent.clearLayoutManagerState){
//                    clearLayoutManagerState()
//                }
//                listInteractors.searchInvitations.searchInvitations(
////                    query = getSearchQuery(),
////                    filterAndOrder = getOrder() + getFilter(),
////                    page = getPage(),
//                    stateEvent = stateEvent
//                )
//            }
//
////            is GetNumInvitationsInCacheEvent -> {
////                invitationInteractors.getNumInvitations.getNumInvitations(
////                    stateEvent = stateEvent
////                )
////            }
////
////            is CreateStateMessageEvent -> {
////                emitStateMessageEvent(
////                    stateMessage = stateEvent.stateMessage,
////                    stateEvent = stateEvent
////                )
////            }
//
//            else -> {
//                emitInvalidStateEvent(stateEvent)
//            }
//        }
//        launchJob(stateEvent, job)
//    }
//
////    private fun removeSelectedInvitationsFromList(){
////        val update = getCurrentViewStateOrNew()
////        update.invitationList?.removeAll(getSelectedInvitations())
////        setViewState(update)
////        clearSelectedInvitations()
////    }
//
////    fun deleteInvitations(){
////        if(getSelectedInvitations().size > 0){
////            setStateEvent(DeleteMultipleInvitationsEvent(getSelectedInvitations()))
////            removeSelectedInvitationsFromList()
////        }
////        else{
////            setStateEvent(
////                CreateStateMessageEvent(
////                    stateMessage = StateMessage(
////                        response = Response(
////                            message = DELETE_NOTES_YOU_MUST_SELECT,
////                            uiComponentType = UIComponentType.Toast(),
////                            messageType = MessageType.Info()
////                        )
////                    )
////                )
////            )
////        }
////    }
//
//    fun getSelectedInvitations() = invitationListInteractionManager.getSelectedInvitations()
//
//    fun setToolbarState(state: InvitationListToolbarState)
//            = invitationListInteractionManager.setToolbarState(state)
//
//    fun isMultiSelectionStateActive()
//            = invitationListInteractionManager.isMultiSelectionStateActive()
//
//    override fun initNewViewState(): _InvitationListViewState {
//        return _InvitationListViewState()
//    }
//
////    fun getFilter(): String {
////        return getCurrentViewStateOrNew().filter
////            ?: NOTE_FILTER_DATE_CREATED
////    }
////
////    fun getOrder(): String {
////        return getCurrentViewStateOrNew().order
////            ?: NOTE_ORDER_DESC
////    }
//
//    fun getSearchQuery(): String {
//        return getCurrentViewStateOrNew().searchQuery
//            ?: return ""
//    }
//
//    private fun getPage(): Int{
//        return getCurrentViewStateOrNew().page
//            ?: return 1
//    }
//
//    private fun setInvitationListData(invitationList: ArrayList<Invitation>){
//        val update = getCurrentViewStateOrNew()
//        update.invitationList = invitationList
//        setViewState(update)
//    }
//
//    fun setQueryExhausted(isExhausted: Boolean){
//        val update = getCurrentViewStateOrNew()
//        update.isQueryExhausted = isExhausted
//        setViewState(update)
//    }
//
//    // can be selected from Recyclerview or created new from dialog
//    fun setInvitation(invitation: Invitation?){
//        val update = getCurrentViewStateOrNew()
//        update.newInvitation = invitation
//        setViewState(update)
//    }
//
//    fun setQuery(query: String?){
//        val update =  getCurrentViewStateOrNew()
//        update.searchQuery = query
//        setViewState(update)
//    }
//
//
////    // if a invitation is deleted and then restored, the id will be incorrect.
////    // So need to reset it here.
////    private fun setRestoredInvitationId(restoredInvitation: Invitation){
////        val update = getCurrentViewStateOrNew()
////        update.invitationList?.let { invitationList ->
////            for((index, invitation) in invitationList.withIndex()){
////                if(invitation.title.equals(restoredInvitation.title)){
////                    invitationList.remove(invitation)
////                    invitationList.add(index, restoredInvitation)
////                    update.invitationList = invitationList
////                    break
////                }
////            }
////        }
////        setViewState(update)
////    }
//
////    fun isDeletePending(): Boolean{
////        val pendingInvitation = getCurrentViewStateOrNew().invitationPendingDelete
////        if(pendingInvitation != null){
////            setStateEvent(
////                CreateStateMessageEvent(
////                    stateMessage = StateMessage(
////                        response = Response(
////                            message = DELETE_PENDING_ERROR,
////                            uiComponentType = UIComponentType.Toast(),
////                            messageType = MessageType.Info()
////                        )
////                    )
////                )
////            )
////            return true
////        }
////        else{
////            return false
////        }
////    }
//
////    fun beginPendingDelete(invitation: Invitation){
////        setInvitationPendingDelete(invitation)
////        removePendingInvitationFromList(invitation)
////        setStateEvent(
////            DeleteInvitationEvent(
////                invitation = invitation
////            )
////        )
////    }
//
////    private fun removePendingInvitationFromList(invitation: Invitation?){
////        val update = getCurrentViewStateOrNew()
////        val list = update.invitationList
////        if(list?.contains(invitation) == true){
////            list.remove(invitation)
////            update.invitationList = list
////            setViewState(update)
////        }
////    }
//
////    fun undoDelete(){
////        // replace invitation in viewstate
////        val update = getCurrentViewStateOrNew()
////        update.invitationPendingDelete?.let { invitation ->
////            if(invitation.listPosition != null && invitation.invitation != null){
////                update.invitationList?.add(
////                    invitation.listPosition as Int,
////                    invitation.invitation as Invitation
////                )
////                setStateEvent(RestoreDeletedInvitationEvent(invitation.invitation as Invitation))
////            }
////        }
////        setViewState(update)
////    }
//
//
////    fun setInvitationPendingDelete(invitation: Invitation?){
////        val update = getCurrentViewStateOrNew()
////        if(invitation != null){
////            update.invitationPendingDelete = InvitationPendingDelete(
////                invitation = invitation,
////                listPosition = findListPositionOfInvitation(invitation)
////            )
////        }
////        else{
////            update.invitationPendingDelete = null
////        }
////        setViewState(update)
////    }
//
//    private fun findListPositionOfInvitation(invitation: Invitation?): Int {
//        val viewState = getCurrentViewStateOrNew()
//        viewState.invitationList?.let { invitationList ->
//            for((index, item) in invitationList.withIndex()){
//                if(item.id == invitation?.id){
//                    return index
//                }
//            }
//        }
//        return 0
//    }
//
////    private fun setNumInvitationsInCache(numInvitations: Int){
////        val update = getCurrentViewStateOrNew()
////        update.numInvitationsInCache = numInvitations
////        setViewState(update)
////    }
//
//    fun createNewInvitation(
//        safeId: Int,
//        recipientId: Int
//    ) = factory.createRequest(safeId, recipientId)
//
//    fun getInvitationListSize() = getCurrentViewStateOrNew().invitationList?.size?: 0
//
////    private fun getNumInvitationsInCache() = getCurrentViewStateOrNew().numInvitationsInCache?: 0
//
////    fun isPaginationExhausted() = getInvitationListSize() >= getNumInvitationsInCache()
//
//    private fun resetPage(){
//        val update = getCurrentViewStateOrNew()
//        update.page = 1
//        setViewState(update)
//    }
//
//    // for debugging
//    fun getActiveJobs() = dataChannelManager.getActiveJobs()
//
//    fun isQueryExhausted(): Boolean{
//        printLogD("InvitationListViewModel",
//            "is query exhasuted? ${getCurrentViewStateOrNew().isQueryExhausted?: true}")
//        return getCurrentViewStateOrNew().isQueryExhausted?: true
//    }
//
//    fun clearList(){
//        printLogD("ListViewModel", "clearList")
//        val update = getCurrentViewStateOrNew()
//        update.invitationList = ArrayList()
//        setViewState(update)
//    }
//
//    // workaround for tests
//    // can't submit an empty string because SearchViews SUCK
//    fun clearSearchQuery(){
//        setQuery("")
//        clearList()
//        loadFirstPage()
//    }
//
//    fun loadFirstPage() {
//        setQueryExhausted(false)
//        resetPage()
//        setStateEvent(SearchInvitationsEvent())
//        printLogD("InvitationListViewModel",
//            "loadFirstPage: ${getCurrentViewStateOrNew().searchQuery}")
//    }
//
//    fun nextPage(){
//        if(!isQueryExhausted()){
//            printLogD("InvitationListViewModel", "attempting to load next page...")
//            clearLayoutManagerState()
//            incrementPageNumber()
//            setStateEvent(SearchInvitationsEvent())
//        }
//    }
//
//    private fun incrementPageNumber(){
//        val update = getCurrentViewStateOrNew()
//        val page = update.copy().page ?: 1
//        update.page = page.plus(1)
//        setViewState(update)
//    }
//
//    fun retrieveNumInvitationsInCache(){
//        setStateEvent(GetNumInvitationsInCacheEvent())
//    }
//
//    fun refreshSearchQuery(){
//        setQueryExhausted(false)
//        setStateEvent(SearchInvitationsEvent(false))
//    }
//
//    fun getLayoutManagerState(): Parcelable? {
//        return getCurrentViewStateOrNew().layoutManagerState
//    }
//
//    fun setLayoutManagerState(layoutManagerState: Parcelable){
//        val update = getCurrentViewStateOrNew()
//        update.layoutManagerState = layoutManagerState
//        setViewState(update)
//    }
//
//    fun clearLayoutManagerState(){
//        val update = getCurrentViewStateOrNew()
//        update.layoutManagerState = null
//        setViewState(update)
//    }
//
//    fun addOrRemoveInvitationFromSelectedList(invitation: Invitation)
//            = invitationListInteractionManager.addOrRemoveInvitationFromSelectedList(invitation)
//
//    fun isInvitationSelected(invitation: Invitation): Boolean
//            = invitationListInteractionManager.isInvitationSelected(invitation)
//
//    fun clearSelectedInvitations() = invitationListInteractionManager.clearSelectedInvitations()
//
//    fun setInvitationFilter(filter: String?){
//        filter?.let{
//            val update = getCurrentViewStateOrNew()
//            update.filter = filter
//            setViewState(update)
//        }
//    }
//
//    fun setInvitationOrder(order: String?){
//        val update = getCurrentViewStateOrNew()
//        update.order = order
//        setViewState(update)
//    }
//
//    fun saveFilterOptions(filter: String, order: String){
//        editor.putString(NOTE_FILTER, filter)
//        editor.apply()
//
//        editor.putString(NOTE_ORDER, order)
//        editor.apply()
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
