package com.cryptoexchanges.domain.repository

import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.model.ExchangeDetail

interface ExchangeRepository {
    suspend fun getExchanges(): DomainResult<List<Exchange>>
    suspend fun getExchangeDetail(exchangeId: Int): DomainResult<ExchangeDetail>
}
