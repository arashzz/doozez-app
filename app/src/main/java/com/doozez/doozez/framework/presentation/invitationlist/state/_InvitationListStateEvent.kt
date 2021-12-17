//package com.doozez.doozez.framework.presentation.invitationlist.state
//
//import com.doozez.doozez.business.domain.models.invitation.Invitation
//import com.doozez.doozez.business.domain.state.StateEvent
//import com.doozez.doozez.business.domain.state.StateMessage
//
//sealed class InvitationListStateEvent: StateEvent {
//
//    class InvitationListEvent(
//        val title: String
//    ): InvitationListStateEvent() {
//
//        override fun errorInfo(): String {
//            return "Error inserting new invitation."
//        }
//
//        override fun eventName(): String {
//            return "InsertNewInvitationEvent"
//        }
//
//        override fun shouldDisplayProgressBar() = true
//    }
//
////    // for testing
////    class InsertMultipleInvitationsEvent(
////        val numInvitations: Int
////    ): InvitationListStateEvent() {
////
////        override fun errorInfo(): String {
////            return "Error inserting the invitations."
////        }
////
////        override fun eventName(): String {
////            return "InsertMultipleInvitationsEvent"
////        }
////
////        override fun shouldDisplayProgressBar() = true
////    }
//
//    class DeleteInvitationEvent(
//        val invitation: Invitation
//    ): InvitationListStateEvent(){
//
//        override fun errorInfo(): String {
//            return "Error deleting invitation."
//        }
//
//        override fun eventName(): String {
//            return "DeleteInvitationEvent"
//        }
//
//        override fun shouldDisplayProgressBar() = true
//    }
//
//    class DeleteMultipleInvitationsEvent(
//        val invitations: List<Invitation>
//    ): InvitationListStateEvent(){
//
//        override fun errorInfo(): String {
//            return "Error deleting the selected invitations."
//        }
//
//        override fun eventName(): String {
//            return "DeleteMultipleInvitationsEvent"
//        }
//
//        override fun shouldDisplayProgressBar() = true
//    }
//
//    class RestoreDeletedInvitationEvent(
//        val invitation: Invitation
//    ): InvitationListStateEvent() {
//
//        override fun errorInfo(): String {
//            return "Error restoring the invitation that was deleted."
//        }
//
//        override fun eventName(): String {
//            return "RestoreDeletedInvitationEvent"
//        }
//
//        override fun shouldDisplayProgressBar() = false
//    }
//
//    class SearchInvitationsEvent(
//        val clearLayoutManagerState: Boolean = true
//    ): InvitationListStateEvent(){
//
//        override fun errorInfo(): String {
//            return "Error getting list of invitations."
//        }
//
//        override fun eventName(): String {
//            return "SearchInvitationsEvent"
//        }
//
//        override fun shouldDisplayProgressBar() = true
//    }
//
//    class GetNumInvitationsInCacheEvent: InvitationListStateEvent(){
//
//        override fun errorInfo(): String {
//            return "Error getting the number of invitations from the cache."
//        }
//
//        override fun eventName(): String {
//            return "GetNumInvitationsInCacheEvent"
//        }
//
//        override fun shouldDisplayProgressBar() = true
//    }
//
//    class CreateStateMessageEvent(
//        val stateMessage: StateMessage
//    ): InvitationListStateEvent(){
//
//        override fun errorInfo(): String {
//            return "Error creating a new state message."
//        }
//
//        override fun eventName(): String {
//            return "CreateStateMessageEvent"
//        }
//
//        override fun shouldDisplayProgressBar() = false
//    }
//
//}
//
//
