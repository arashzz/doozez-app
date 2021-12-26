package com.doozez.doozez.enums

enum class PaymentMethodStatus(val code: String, val description: String) {
    UNKNOWN("", "Unknown"),
    PENDING_EXTERNAL_APPROVAL("PEA", "Pending Approval"),
    EXTERNAL_APPROVAL_FAILED("EAF", "Approval Failed"),
    EXTERNAL_APPROVAL_SUCCESSFUL("EAS", "Approval Successful"),
    EXTERNALLY_CREATED("EXC", "Created"),
    EXTERNALLY_SUBMITTED("EXS", "Submitted"),
    EXTERNALLY_ACTIVATED("EXA", "Active");

    companion object {
        private val map = values().associateBy(PaymentMethodStatus::code)
        fun fromCode(type: String): PaymentMethodStatus {
            val status = map[type]
            if(status != null) {
                return status
            }
            return UNKNOWN
        }
    }
}