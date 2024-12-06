package com.xheghun.xchange.di

import com.google.gson.GsonBuilder
import com.xheghun.xchange.BuildConfig
import com.xheghun.xchange.data.api.ExchangeApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.exchangeratesapi.io/"

fun networkModule() = module {
    single {
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

    single {
        Interceptor { chain ->
            val originalRequest = chain.request();

            //add api key to every request
            val modifiedUrl = originalRequest.url.newBuilder()
                .addQueryParameter("access_token", BuildConfig.EXCHANGE_API_KEY)
                .build()

            val modifiedRequest = originalRequest.newBuilder()
                .url(modifiedUrl)
                .build()

            chain.proceed(modifiedRequest)
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    } // client

    single { GsonBuilder().create() }

    single { GsonConverterFactory.create(get()) } // gson converter

    single {
        Retrofit.Builder()
            .addConverterFactory(get<GsonConverterFactory>())
            .client(get<OkHttpClient>())
            .baseUrl(BASE_URL)
            .build()
    } // retrofit

    single { get<Retrofit>().create(ExchangeApiService::class.java) } // api service
}