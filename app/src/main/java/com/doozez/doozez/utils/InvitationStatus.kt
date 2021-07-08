package com.doozez.doozez.utils

object InvitationStatus {
    const val PENDING = "PND"
    const val ACCEPTED = "ACC"
    const val DECLINED = "DEC"

    fun getStatusFromResponse(respStatus: String): String {
        var status = PENDING
        if (respStatus == "Accepted") {
            status = ACCEPTED
        } else if (respStatus == "Declined") {
            status = DECLINED
        }
        return status
    }
}