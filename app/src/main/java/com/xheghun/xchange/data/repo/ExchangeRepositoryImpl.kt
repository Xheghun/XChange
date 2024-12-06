package com.xheghun.xchange.data.repo

import com.xheghun.xchange.data.api.ExchangeApiService
import com.xheghun.xchange.data.cache.Cache
import com.xheghun.xchange.data.model.ExchangeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExchangeRepositoryImpl(
    private val exchangeService: ExchangeApiService,
    private val cache: Cache<ExchangeResult>
) :
    ExchangeRepository {
    override suspend fun getExchange(): Result<ExchangeResult> =
        withContext(Dispatchers.IO) {
            runCatching {
                cache.get() ?: exchangeService.getLatestExchange()
            }
        }

}