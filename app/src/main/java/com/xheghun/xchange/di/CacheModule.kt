package com.xheghun.xchange.di

import com.xheghun.xchange.data.cache.Cache
import com.xheghun.xchange.data.cache.LocalCache
import com.xheghun.xchange.data.model.ExchangeResult
import org.koin.dsl.module

fun cacheModule() = module {
    single { LocalCache<ExchangeResult>() as Cache<ExchangeResult>}
}