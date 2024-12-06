package com.xheghun.xchange.di

import com.xheghun.xchange.data.repo.ExchangeRepository
import com.xheghun.xchange.data.repo.ExchangeRepositoryImpl
import org.koin.dsl.module

fun appModule() = module {
    single { ExchangeRepositoryImpl(get(), get()) as ExchangeRepository }
}