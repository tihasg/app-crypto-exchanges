package com.cryptoexchanges.domain.usecase

import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.ExchangeDetail
import com.cryptoexchanges.domain.repository.ExchangeRepository

class GetExchangeDetailUseCase(private val repository: ExchangeRepository) {
    suspend operator fun invoke(exchangeId: Int): DomainResult<ExchangeDetail> =
        repository.getExchangeDetail(exchangeId)
}
