package com.doozez.doozez.utils

object InvitationStatus {
    const val PENDING = "PND"
    const val ACCEPTED = "ACC"
    const val DECLINED = "DEC"
    const val CANCELLED = "RBS"

    fun getStatusFromResponse(respStatus: String): String {
        var status = PENDING
        if (respStatus == "Accepted") {
            status = ACCEPTED
        } else if (respStatus == "Declined") {
            status = DECLINED
        } else if (respStatus == "RemovedBySender") {
            status = CANCELLED
        }
        return status
    }
}