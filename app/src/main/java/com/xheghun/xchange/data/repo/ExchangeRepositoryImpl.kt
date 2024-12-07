package com.xheghun.xchange.data.repo

import com.xheghun.xchange.data.api.ExchangeApiService
import com.xheghun.xchange.data.cache.Cache
import com.xheghun.xchange.data.model.ExchangeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val CACHE_KEY = "exchange_cache"
private val COROUTINE_CONTEXT = Dispatchers.IO

class ExchangeRepositoryImpl(
    private val exchangeService: ExchangeApiService,
    private val cache: Cache<ExchangeResult>
) :
    ExchangeRepository {
    override suspend fun getExchange(): Result<ExchangeResult> =
        withContext(COROUTINE_CONTEXT) {
            runCatching {
                cache.get(CACHE_KEY) ?: exchangeService.getLatestExchange().also {
                    cache.put(CACHE_KEY, it)
                }
            }
        }
}