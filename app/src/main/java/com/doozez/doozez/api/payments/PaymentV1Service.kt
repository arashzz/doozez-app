package com.doozez.doozez.api.payments

import retrofit2.Call
import retrofit2.http.GET

interface PaymentV1Service {
    @GET("v1/payment-methods/")
    fun getPayments(): Call<List<PaymentDetailResp>>
}