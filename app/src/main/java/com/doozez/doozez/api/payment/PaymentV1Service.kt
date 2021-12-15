package com.doozez.doozez.api.payment

import com.doozez.doozez.api.PaginatedListResponse
import retrofit2.Call
import retrofit2.http.*

interface PaymentV1Service {
    @GET("v1/payments/")
    fun getPayments(): Call<PaginatedListResponse<PaymentDetailResp>>
}