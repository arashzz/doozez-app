package com.doozez.doozez.ui.payment.listeners

import com.doozez.doozez.api.payments.PaymentDetailResp

interface PaymentMethodItemListener {
    fun paymentMethodClicked(paymentMethod: PaymentDetailResp)
}