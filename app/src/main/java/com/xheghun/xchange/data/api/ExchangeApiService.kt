package com.xheghun.xchange.data.api

import com.xheghun.xchange.data.model.ExchangeResult
import retrofit2.http.GET

interface ExchangeApiService {
    @GET("v1/latest")
    suspend fun getLatestExchange(): ExchangeResult
}