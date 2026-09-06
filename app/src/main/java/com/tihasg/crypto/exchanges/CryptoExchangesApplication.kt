package com.tihasg.crypto.exchanges

import android.app.Application
import com.tihasg.crypto.exchanges.di.dataModule
import com.tihasg.crypto.exchanges.di.domainModule
import com.tihasg.crypto.exchanges.di.networkModule
import com.tihasg.crypto.exchanges.di.presentationModule
import com.tihasg.crypto.exchanges.di.workModule
import com.tihasg.crypto.exchanges.sync.schedulePeriodicExchangeSync
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class CryptoExchangesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CryptoExchangesApplication)
            workManagerFactory()
            modules(networkModule, dataModule, domainModule, presentationModule, workModule)
        }
        schedulePeriodicExchangeSync(this)
    }
}
