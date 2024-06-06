package com.example.core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.core.Util
import com.example.core.network.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl() = Util.Base


    @Singleton
    @Provides
    fun providesOkHttpClient(
        @ApplicationContext context:Context
    ):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(context))
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(
        client:OkHttpClient,
        @Named("BASE_URL") base:String
    ):Retrofit
    {
        val api = Retrofit.Builder()
            .baseUrl(base)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return api
    }

    @Singleton
    @Provides
    fun providesApiService(
        retrofit: Retrofit
    ): ApiInterface = retrofit.create(ApiInterface::class.java)

}