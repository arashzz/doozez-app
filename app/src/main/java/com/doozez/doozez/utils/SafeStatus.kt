package com.doozez.doozez.utils

enum class SafeStatus(val code:String, val description: String) {
    NA("N/A", "Not Applicable"),
    PENDING_PARTICIPANTS("PPT", "Waiting for Participants"),
    PENDING_DRAW("PDR", "Pending Draw"),
    PENDING_ENTRY_PAYMENT("PEP", "Pending Entry Payment"),
    STARTING("STG", "Starting Safe"),
    ACTIVE("ACT", "Active"),
    COMPLETE("CPT", "Completed");

    companion object {
        fun getStatusForCode(code: String): SafeStatus {
            var status = NA
            when(code) {
                PENDING_PARTICIPANTS.code -> status = PENDING_PARTICIPANTS
                PENDING_DRAW.code -> status = PENDING_DRAW
                PENDING_ENTRY_PAYMENT.code -> status = PENDING_ENTRY_PAYMENT
                ACTIVE.code -> status = ACTIVE
                COMPLETE.code -> status = COMPLETE
                STARTING.code -> status = STARTING
            }
            return status
        }
    }
}

//object SafeStatus {
//    const val PENDING_PARTICIPANTS = "PPT"
//    const val PENDING_DRAW = "PDR"
//    const val PENDING_ENTRY_PAYMENT = "PEP"
//
//    const val ACTIVE = "ACT"
//    const val COMPLETE = "CPT"
//
//    fun getStatusTextForCode(code: String): String {
//        var status = "N/A"
//        when(code) {
//            PENDING_PARTICIPANTS -> status = "Waiting for Participants"
//            PENDING_DRAW -> status = "Pending Draw"
//            PENDING_ENTRY_PAYMENT -> status = "Pending Entry Payment"
//            ACTIVE -> status = "Active"
//            COMPLETE -> status = "Completed"
//        }
//        return status
//    }
//}