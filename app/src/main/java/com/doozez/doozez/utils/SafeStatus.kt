package com.doozez.doozez.utils

object SafeStatus {
    const val PENDING_PARTICIPANTS = "PPT"
    const val PENDING_DRAW = "PDR"
    const val PENDING_ENTRY_PAYMENT = "PEP"
    const val ACTIVE = "ACT"
    const val COMPLETE = "CPT"

    fun getStatusTextForCode(code: String): String {
        var status = "N/A"
        when(code) {
            PENDING_PARTICIPANTS -> status = "Waiting for Participants"
            PENDING_DRAW -> status = "Pending Draw"
            PENDING_ENTRY_PAYMENT -> status = "Pending Entry Payment"
            ACTIVE -> status = "Active"
            COMPLETE -> status = "Completed"
        }
        return status
    }
}