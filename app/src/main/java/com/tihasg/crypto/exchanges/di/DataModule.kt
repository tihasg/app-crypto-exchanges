package com.tihasg.crypto.exchanges.di

import com.cryptoexchanges.data.remote.ExchangeRemoteDataSource
import com.cryptoexchanges.data.repository.ExchangeRepositoryImpl
import com.cryptoexchanges.data.repository.FakeExchangeRepository
import com.cryptoexchanges.domain.repository.ExchangeRepository
import com.tihasg.crypto.exchanges.BuildConfig
import org.koin.dsl.module

val dataModule = module {
    single { ExchangeRemoteDataSource(get()) }
    single<ExchangeRepository> {
        if (BuildConfig.CMC_API_KEY.isBlank()) {
            FakeExchangeRepository()
        } else {
            ExchangeRepositoryImpl(get())
        }
    }
}
