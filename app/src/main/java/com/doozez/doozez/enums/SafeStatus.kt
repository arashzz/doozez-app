package com.doozez.doozez.enums

enum class SafeStatus(val code:String, val description: String) {
    UNKNOWN("", "Unknown"),
    PENDING_PARTICIPANTS("PPT", "Waiting for Participants"),
    PENDING_DRAW("PDR", "Pending Draw"),
    PENDING_ENTRY_PAYMENT("PEP", "Pending Entry Payment"),
    STARTING("STG", "Starting Safe"),
    STARTED("STD", "Started"),
    ACTIVE("ACT", "Active"),
    COMPLETE("CPT", "Completed");

    companion object {
        private val map = values().associateBy(SafeStatus::code)
        fun fromCode(type: String): SafeStatus {
            val status = map[type]
            if(status != null) {
                return status
            }
            return UNKNOWN
        }
    }
}