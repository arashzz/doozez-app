package com.doozez.doozez.enums

import com.doozez.doozez.R

enum class PaymentMethodType(val displayName:String, val resId:Int) {
    UNKNOWN("Unknown", R.drawable.ic_baseline_close_24),
    DIRECT_DEBIT("Direct Debit", R.drawable.ic_baseline_credit_card_36),
    PAYPAL("Paypal", R.drawable.ic_baseline_credit_card_36),
    CC_VISA("Visa", R.drawable.ic_baseline_credit_card_36),
    CC_MASTER("Mastercard", R.drawable.ic_baseline_credit_card_36),
    CC_AMEX("American Express", R.drawable.ic_baseline_credit_card_36);

    companion object {
        private val map = values().associateBy(PaymentMethodType::name)
        fun fromCode(type: String): PaymentMethodType {
            val status = map[type]
            if(status != null) {
                return status
            }
            return UNKNOWN
        }
    }
}