package com.cryptoexchanges.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class ExchangeLocalDataSource(private val dataStore: DataStore<Preferences>) {

    fun observeExchanges(): Flow<List<ExchangeCacheDto>> = dataStore.data.map { preferences ->
        preferences[CACHED_EXCHANGES_KEY]?.let { json.decodeFromString<List<ExchangeCacheDto>>(it) }
            .orEmpty()
    }

    suspend fun saveExchanges(exchanges: List<ExchangeCacheDto>) {
        dataStore.edit { it[CACHED_EXCHANGES_KEY] = json.encodeToString(exchanges) }
    }

    private companion object {
        val CACHED_EXCHANGES_KEY = stringPreferencesKey("cached_exchanges")
        val json = Json { ignoreUnknownKeys = true }
    }
}
