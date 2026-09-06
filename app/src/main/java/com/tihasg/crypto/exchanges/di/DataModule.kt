package com.tihasg.crypto.exchanges.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.cryptoexchanges.data.local.ExchangeLocalDataSource
import com.cryptoexchanges.data.remote.ExchangeRemoteDataSource
import com.cryptoexchanges.data.repository.ExchangeRepositoryImpl
import com.cryptoexchanges.domain.repository.ExchangeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File

private const val EXCHANGES_CACHE_FILE_NAME = "exchanges_cache.preferences_pb"

val dataModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { File(androidContext().filesDir, EXCHANGES_CACHE_FILE_NAME) },
        )
    }
    single { ExchangeLocalDataSource(get()) }
    single { ExchangeRemoteDataSource(get()) }
    single<ExchangeRepository> { ExchangeRepositoryImpl(get(), get()) }
}
