package com.doozez.doozez.enums

enum class PaymentStatus(val code: String, val description: String) {
    UNKNOWN("","Unknown"),
    PENDING_SUBMISSION("pending_submission", "Pending Submission"),
    SUBMITTED("submitted", "Submitted"),
    CONFIRMED("confirmed", "Confirmed"),
    FAILED("failed", "Failed"),
    CANCELLED("cancelled", "Cancelled"),
    CUSTOMER_APPROVAL_DENIED("customer_approval_denied", "Customer Approval Denied"),
    CHARGED_BACK("charged_back", "Charged Back");

    companion object {
        private val map = values().associateBy(PaymentStatus::code)
        fun fromCode(type: String): PaymentStatus {
            val status = map[type]
            if(status != null) {
                return status
            }
            return UNKNOWN
        }
    }
}