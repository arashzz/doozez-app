package com.doozez.doozez.ui.payment.listeners

import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp

interface PaymentMethodItemListener {
    fun paymentMethodClicked(paymentMethod: PaymentMethodDetailResp)
}