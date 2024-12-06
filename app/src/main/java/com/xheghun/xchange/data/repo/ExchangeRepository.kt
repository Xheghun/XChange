package com.xheghun.xchange.data.repo

import com.xheghun.xchange.data.model.ExchangeResult

interface ExchangeRepository {
    suspend fun getExchange(): Result<ExchangeResult>
}