package com.doozez.doozez.utils

import com.doozez.doozez.R

enum class InvitationStatus(val code:String, val description: String, val colorId: Int, val resId: Int) {
    NA("N/A", "Not Applicable", R.color.red, R.drawable.ic_baseline_clear_24),
    PENDING("PND", "Pending", R.color.yellow, R.drawable.ic_baseline_access_time_24),
    ACCEPTED("ACC", "Accepted", R.color.green, R.drawable.ic_round_check_24),
    DECLINED("DEC", "Declined", R.color.red, R.drawable.ic_baseline_clear_24),
    CANCELLED("RBS", "Cancelled", R.color.red, R.drawable.ic_baseline_clear_24);

    companion object {
        fun fromCode(code: String): InvitationStatus {
            var status = NA
            when(code) {
                PENDING.code -> status = PENDING
                ACCEPTED.code -> status = ACCEPTED
                DECLINED.code -> status = DECLINED
                CANCELLED.code -> status = CANCELLED
            }
            return status
        }
    }
}