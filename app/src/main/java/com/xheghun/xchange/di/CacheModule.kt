package com.xheghun.xchange.di

import android.content.Context
import com.xheghun.xchange.data.cache.Cache
import com.xheghun.xchange.data.cache.LocalCache
import com.xheghun.xchange.data.model.ExchangeResult
import org.koin.dsl.module

fun cacheModule(context: Context) = module {
    single {
        LocalCache(
            context,
            get(),
            ExchangeResult::class.java,
            21_600_000 //6 hours
        ) as Cache<ExchangeResult>
    }
}