package com.tihasg.crypto.exchanges.di

import com.tihasg.crypto.exchanges.sync.ExchangeSyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workModule = module {
    worker { ExchangeSyncWorker(androidContext(), get(), get()) }
}
