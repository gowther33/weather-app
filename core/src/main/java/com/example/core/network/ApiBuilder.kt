package com.example.core.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.core.Util
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiBuilder(val context: Context) {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ChuckerInterceptor(context))
        .build()

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(Util.Base)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

}