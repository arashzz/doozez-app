package com.doozez.doozez.enums

import com.doozez.doozez.R

enum class InvitationStatus(val code:String, val description: String) {
    UNKNOWN("N/A", "Unknown"),
    PENDING("PND", "Pending"),
    ACCEPTED("ACC", "Accepted"),
    DECLINED("DEC", "Declined"),
    CANCELLED("RBS", "Cancelled");

    companion object {
        private val map = values().associateBy(InvitationStatus::code)
        fun fromCode(type: String): InvitationStatus {
            val status = map[type]
            if(status != null) {
                return status
            }
            return UNKNOWN
        }
    }
}