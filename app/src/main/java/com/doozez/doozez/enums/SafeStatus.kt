package com.doozez.doozez.enums

enum class SafeStatus(val code:String, val description: String) {
    UNKNOWN("", "Unknown"),
    PENDING_PARTICIPANTS("PPT", "Not Started"),
    PENDING_DRAW("PDR", "Starting"),
    PENDING_ENTRY_PAYMENT("PEP", "Starting"),
    STARTING("STG", "Starting"),
    STARTED("STD", "Started"),
    ACTIVE("ACT", "Running"),
    COMPLETE("CPT", "Finished");

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