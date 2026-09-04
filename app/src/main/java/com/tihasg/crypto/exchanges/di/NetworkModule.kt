package com.tihasg.crypto.exchanges.di

import com.cryptoexchanges.core.network.NetworkConfig
import com.cryptoexchanges.core.network.OkHttpClientFactory
import com.cryptoexchanges.core.network.RetrofitFactory
import com.cryptoexchanges.data.remote.ExchangeApiService
import com.tihasg.crypto.exchanges.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit

private const val CMC_BASE_URL = "https://pro-api.coinmarketcap.com/"

val networkModule = module {
    single {
        NetworkConfig(
            baseUrl = CMC_BASE_URL,
            apiKey = BuildConfig.CMC_API_KEY,
            enableLogging = BuildConfig.DEBUG,
        )
    }
    single { OkHttpClientFactory.create(get()) }
    single { RetrofitFactory.create(CMC_BASE_URL, get()) }
    single { get<Retrofit>().create(ExchangeApiService::class.java) }
}
