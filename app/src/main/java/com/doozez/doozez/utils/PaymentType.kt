package com.doozez.doozez.utils

enum class PaymentType {
    DIRECT_DEBIT;

    companion object {
        fun getPaymentName(type: String): String {
            return getPaymentName(PaymentType.DIRECT_DEBIT)
        }
        fun getPaymentName(type: PaymentType): String {
            return "Direct Debit"
        }
    }
}