package com.doozez.doozez.api.payments

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentV1Service {
    @GET("v1/payment-methods/{id}")
    fun getPaymentById(@Path("id") id: Int): Call<PaymentDetailResp>

    @GET("v1/payment-methods/")
    fun getPayments(): Call<List<PaymentDetailResp>>

    @POST("v1/payment-methods/")
    fun createPayment(@Body body: PaymentCreateReq): Call<PaymentDetailResp>
}