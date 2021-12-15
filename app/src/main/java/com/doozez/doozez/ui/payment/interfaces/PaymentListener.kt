package com.doozez.doozez.ui.payment.interfaces

import com.doozez.doozez.api.payment.PaymentDetailResp

interface PaymentListener {
    fun onClickListener(payment: PaymentDetailResp)
}