package com.doozez.doozez.utils

enum class PaymentType {
    DIRECT_DEBIT;

    companion object {
        fun getPaymentName(type: PaymentType): String {
            return "Direct Debit"
        }
    }
}