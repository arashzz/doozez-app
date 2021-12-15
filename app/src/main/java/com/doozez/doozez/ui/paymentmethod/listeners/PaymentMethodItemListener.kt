package com.doozez.doozez.ui.paymentmethod.listeners

import com.doozez.doozez.api.paymentMethod.PaymentMethodDetailResp

interface PaymentMethodItemListener {
    fun paymentMethodClicked(paymentMethod: PaymentMethodDetailResp)
}