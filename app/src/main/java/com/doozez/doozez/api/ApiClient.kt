package com.doozez.doozez.api

import com.doozez.doozez.api.auth.AuthV1Service
import com.doozez.doozez.api.device.DeviceV1Service
import com.doozez.doozez.api.invitation.InvitationV1Service
import com.doozez.doozez.api.job.JobV1Service
import com.doozez.doozez.api.participation.ParticipationV1Service
import com.doozez.doozez.api.payment.PaymentV1Service
import com.doozez.doozez.api.paymentMethod.PaymentMethodV1Service
import com.doozez.doozez.api.safe.SafeV1Service
import com.doozez.doozez.api.user.UserV1Service
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL: String = "http://10.0.2.2:8000"

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient
            .Builder()
            .addInterceptor(DoozInterceptor())
            .build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    val invitationService : InvitationV1Service by lazy{
        retrofit.create(InvitationV1Service::class.java)
    }

    val safeService: SafeV1Service by lazy {
        retrofit.create(SafeV1Service::class.java)
    }

    val userService: UserV1Service by lazy {
        retrofit.create(UserV1Service::class.java)
    }

    val PAYMENT_METHOD_SERVICE: PaymentMethodV1Service by lazy {
        retrofit.create(PaymentMethodV1Service::class.java)
    }

    val participationService: ParticipationV1Service by lazy {
        retrofit.create(ParticipationV1Service::class.java)
    }

    val authService: AuthV1Service by lazy {
        retrofit.create(AuthV1Service::class.java)
    }

    val jobService: JobV1Service by lazy {
        retrofit.create(JobV1Service::class.java)
    }

    val deviceService: DeviceV1Service by lazy {
        retrofit.create(DeviceV1Service::class.java)
    }

    val paymentService: PaymentV1Service by lazy {
        retrofit.create(PaymentV1Service::class.java)
    }
}