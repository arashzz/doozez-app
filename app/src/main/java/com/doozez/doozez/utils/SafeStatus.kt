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