package com.doozez.doozez.utils

enum class PaymentMethodStatus {
    PEA,
    EAS;

    companion object {
        fun getPaymentStatus(status: String): String {
            return when (valueOf(status)) {
                PEA -> "Pending External Approval"
                EAS -> "External Approval Successful"
            }
        }
    }
}