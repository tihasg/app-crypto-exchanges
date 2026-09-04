package com.tihasg.crypto.exchanges.di

import com.tihasg.crypto.exchanges.presentation.detail.ExchangeDetailViewModel
import com.tihasg.crypto.exchanges.presentation.list.ExchangeListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { ExchangeListViewModel(get()) }
    viewModel { (exchangeId: Int) -> ExchangeDetailViewModel(exchangeId, get()) }
}
