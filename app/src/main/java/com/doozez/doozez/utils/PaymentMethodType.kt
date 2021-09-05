package com.doozez.doozez.utils

enum class PaymentMethodType {
    DIRECT_DEBIT;

    companion object {
        fun getPaymentName(type: String): String {
            return getPaymentName(PaymentMethodType.DIRECT_DEBIT)
        }
        fun getPaymentName(methodType: PaymentMethodType): String {
            return "Direct Debit"
        }
    }
}