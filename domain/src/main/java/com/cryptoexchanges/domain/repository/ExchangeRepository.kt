package com.cryptoexchanges.domain.repository

import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.model.ExchangeDetail
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    suspend fun getExchanges(): DomainResult<List<Exchange>>
    fun observeCachedExchanges(): Flow<List<Exchange>>
    suspend fun getExchangeDetail(exchangeId: Int): DomainResult<ExchangeDetail>
}
