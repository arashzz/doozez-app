package com.doozez.doozez.enums

import com.doozez.doozez.R

enum class InvitationStatus(val code:String, val description: String, val colorId: Int, val resId: Int) {
    UNKNOWN("N/A", "Unknown", R.color.red, R.drawable.ic_baseline_clear_24),
    PENDING("PND", "Pending", R.color.yellow, R.drawable.ic_baseline_access_time_24),
    ACCEPTED("ACC", "Accepted", R.color.green, R.drawable.ic_round_check_24),
    DECLINED("DEC", "Declined", R.color.red, R.drawable.ic_baseline_clear_24),
    CANCELLED("RBS", "Cancelled", R.color.red, R.drawable.ic_baseline_clear_24);

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