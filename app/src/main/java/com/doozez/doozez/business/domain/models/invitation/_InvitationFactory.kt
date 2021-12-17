//package com.doozez.doozez.business.domain.models.invitation
//
//import com.doozez.doozez.enums.InvitationAction
//import org.json.JSONObject
//import javax.inject.Singleton
//
//@Singleton
//class InvitationFactory {
//
//    fun createRequest (
//        safeId: Int,
//        recipientId: Int
//    ): InviteCreateReq {
//        return InviteCreateReq(
//            safeId,
//            recipientId
//        )
//    }
//
//    fun createReply (
//        action: InvitationAction,
//        paymentMethodId: Int
//    ): InvitationReply {
//        val paymentMethodIdStr = JSONObject().put("payment_method_id", paymentMethodId).toString()
//        return InvitationReply(
//            action.name,
//            paymentMethodIdStr
//        )
//    }
//}