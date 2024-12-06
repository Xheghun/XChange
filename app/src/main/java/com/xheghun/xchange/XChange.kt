package com.xheghun.xchange

import android.app.Application
import com.xheghun.xchange.di.appModule
import com.xheghun.xchange.di.cacheModule
import com.xheghun.xchange.di.networkModule
import com.xheghun.xchange.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class XChange : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@XChange)
            modules(listOf(
                appModule(),
                viewModelModule(),
                cacheModule(),
                networkModule()
            ))
        }
    }
}