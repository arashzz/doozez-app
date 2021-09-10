package com.doozez.doozez.api.paymentMethod

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentMethodV1Service {
    @GET("v1/payment-methods/{id}")
    fun getPaymentById(@Path("id") id: Int): Call<PaymentMethodDetailResp>

    @GET("v1/payment-methods/")
    fun getPayments(): Call<List<PaymentMethodDetailResp>>

    @POST("v1/payment-methods/")
    fun createPayment(@Body body: PaymentMethodCreateReq): Call<PaymentMethodDetailResp>
}