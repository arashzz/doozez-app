package com.doozez.doozez.utils

enum class PaymentMethodStatus(val code: String, val description: String) {
    UNKNOWN("", "Unknown"),
    PENDING_EXTERNAL_APPROVAL("PEA", "Pending External Approval"),
    EXTERNAL_APPROVAL_FAILED("EAF", "External Approval Failed"),
    EXTERNAL_APPROVAL_SUCCESSFUL("EAS", "External Approval Successful"),
    EXTERNALLY_CREATED("EXC", "Externally Created"),
    EXTERNALLY_SUBMITTED("EXS", "Externally Submitted"),
    EXTERNALLY_ACTIVATED("EXA", "Externally Activated");

    companion object {
        private val map = PaymentMethodStatus.values().associateBy(PaymentMethodStatus::code)
        fun fromCode(type: String): PaymentMethodStatus {
            val status = map[type]
            if(status != null) {
                return status
            }
            return UNKNOWN
        }
    }
}