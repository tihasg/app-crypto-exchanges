package com.cryptoexchanges.domain.usecase

import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.model.map
import com.cryptoexchanges.domain.model.sortedByVolumeDescending
import com.cryptoexchanges.domain.repository.ExchangeRepository

class GetExchangesUseCase(private val repository: ExchangeRepository) {
    suspend operator fun invoke(): DomainResult<List<Exchange>> =
        repository.getExchanges().map { exchanges -> exchanges.sortedByVolumeDescending() }
}
