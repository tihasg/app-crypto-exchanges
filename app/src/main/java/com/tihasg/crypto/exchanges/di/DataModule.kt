package com.tihasg.crypto.exchanges.di

import com.cryptoexchanges.data.remote.ExchangeRemoteDataSource
import com.cryptoexchanges.data.repository.ExchangeRepositoryImpl
import com.cryptoexchanges.domain.repository.ExchangeRepository
import org.koin.dsl.module

val dataModule = module {
    single { ExchangeRemoteDataSource(get()) }
    single<ExchangeRepository> { ExchangeRepositoryImpl(get()) }
}
