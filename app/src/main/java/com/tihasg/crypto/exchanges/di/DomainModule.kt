package com.tihasg.crypto.exchanges.di

import com.cryptoexchanges.domain.usecase.GetExchangeDetailUseCase
import com.cryptoexchanges.domain.usecase.GetExchangesUseCase
import com.cryptoexchanges.domain.usecase.ObserveExchangesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetExchangesUseCase(get()) }
    factory { ObserveExchangesUseCase(get()) }
    factory { GetExchangeDetailUseCase(get()) }
}
