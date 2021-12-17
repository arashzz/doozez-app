package com.doozez.doozez.di

import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.DoozInterceptor
import com.doozez.doozez.business.data.network.abstraction.InvitationNetworkDataSource
import com.doozez.doozez.business.data.network.implementation.InvitationNetworkDataSourceImpl
import com.doozez.doozez.business.domain.models.invitation.Invitation
import com.doozez.doozez.business.domain.util.DateUtil
import com.doozez.doozez.business.domain.util.EntityMapper
import com.doozez.doozez.framework.datasource.network.abstraction.InvitationRetrofitService
import com.doozez.doozez.framework.datasource.network.implementation.InvitationRetrofitServiceImpl
import com.doozez.doozez.framework.datasource.network.mappers.InvitationNetworkMapper
import com.doozez.doozez.framework.datasource.network.model.InvitationNetworkEntity
import com.doozez.doozez.framework.datasource.network.retrofit.InvitationRetrofit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideDateUtil(): DateUtil {
        return DateUtil(SimpleDateFormat())
    }

    @Singleton
    @Provides
    fun provideInvitationNetworkMapper(dateUtil: DateUtil): EntityMapper<InvitationNetworkEntity, Invitation> {
        return InvitationNetworkMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(DoozInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okhttpClient: OkHttpClient, gson:  Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    }



    @Singleton
    @Provides
    fun provideInvitationService(retrofit: Retrofit.Builder): InvitationRetrofit {
        return retrofit
            .build()
            .create(InvitationRetrofit::class.java)
    }

    @Singleton
    @Provides
    fun provideInvitationRetrofitService(
        invitationRetrofit: InvitationRetrofit
    ): InvitationRetrofitService {
        return InvitationRetrofitServiceImpl(invitationRetrofit)
    }

    @Singleton
    @Provides
    fun provideInvitationNetworkDataSource(
        invitationRetrofitService: InvitationRetrofitService,
        networkMapper: InvitationNetworkMapper
    ): InvitationNetworkDataSource{
        return InvitationNetworkDataSourceImpl(invitationRetrofitService, networkMapper)
    }
}