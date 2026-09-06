package com.cryptoexchanges.domain.usecase

import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.model.sortedByVolumeDescending
import com.cryptoexchanges.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveExchangesUseCase(private val repository: ExchangeRepository) {
    operator fun invoke(): Flow<List<Exchange>> =
        repository.observeCachedExchanges()
            .map { exchanges -> exchanges.sortedByVolumeDescending() }
}
