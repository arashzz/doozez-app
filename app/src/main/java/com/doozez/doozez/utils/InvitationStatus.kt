package com.doozez.doozez.utils

import com.doozez.doozez.R

enum class InvitationStatus(val code:String, val description: String, val colorId: Int) {
    NA("N/A", "Not Applicable", R.color.red),
    PENDING("PND", "Pending", R.color.yellow),
    ACCEPTED("ACC", "Accepted", R.color.green),
    DECLINED("DEC", "Declined", R.color.red),
    CANCELLED("RBS", "Cancelled", R.color.red);

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