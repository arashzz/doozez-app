package com.doozez.doozez.enums

enum class ParticipationStatus(val code:String, val description:String) {
    UNKNOWN("", "Unknown"),
    PENDING("PND", "Pending"),
    ACTIVE("ACT", "Active"),
    COMPLETE("CPT", "Complete"),
    PENDING_PAYMENT("PPT", "Pending Payment"),
    LEFT("LEF", "Left");

    companion object {
        private val map = values().associateBy(ParticipationStatus::code)
        fun fromCode(type: String): ParticipationStatus {
            val status = map[type]
            if(status != null) {
                return status
            }
            return UNKNOWN
        }
    }
}