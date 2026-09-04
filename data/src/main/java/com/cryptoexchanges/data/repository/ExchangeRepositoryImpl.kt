package com.cryptoexchanges.data.repository

import com.cryptoexchanges.core.network.NetworkResult
import com.cryptoexchanges.data.mapper.toDomainError
import com.cryptoexchanges.data.mapper.toExchange
import com.cryptoexchanges.data.mapper.toExchangeDetail
import com.cryptoexchanges.data.remote.ExchangeRemoteDataSource
import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.model.ExchangeDetail
import com.cryptoexchanges.domain.repository.ExchangeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ExchangeRepositoryImpl(
    private val remoteDataSource: ExchangeRemoteDataSource,
) : ExchangeRepository {

    override suspend fun getExchanges(): DomainResult<List<Exchange>> = coroutineScope {
        val mapResult = remoteDataSource.getExchangeMap()
        val exchangeMaps = when (mapResult) {
            is NetworkResult.Error -> return@coroutineScope DomainResult.Error(mapResult.error.toDomainError())
            is NetworkResult.Success -> mapResult.data
        }
        if (exchangeMaps.isEmpty()) return@coroutineScope DomainResult.Success(emptyList())

        val ids = exchangeMaps.map { it.id }
        val infoDeferred = async { remoteDataSource.getExchangeInfo(ids) }
        val quotesDeferred = async { remoteDataSource.getExchangeQuotes(ids) }

        val infoResult = infoDeferred.await()
        val infoById = when (infoResult) {
            is NetworkResult.Error -> return@coroutineScope DomainResult.Error(infoResult.error.toDomainError())
            is NetworkResult.Success -> infoResult.data
        }

        val quotesResult = quotesDeferred.await()
        val quotesById = when (quotesResult) {
            is NetworkResult.Error -> return@coroutineScope DomainResult.Error(quotesResult.error.toDomainError())
            is NetworkResult.Success -> quotesResult.data
        }

        val exchanges = exchangeMaps.map { map ->
            toExchange(map, infoById[map.id.toString()], quotesById[map.id.toString()])
        }
        DomainResult.Success(exchanges)
    }

    override suspend fun getExchangeDetail(exchangeId: Int): DomainResult<ExchangeDetail> = coroutineScope {
        val infoDeferred = async { remoteDataSource.getExchangeInfo(listOf(exchangeId)) }
        val marketPairsDeferred = async { remoteDataSource.getExchangeMarketPairs(exchangeId) }

        val infoResult = infoDeferred.await()
        val infoById = when (infoResult) {
            is NetworkResult.Error -> return@coroutineScope DomainResult.Error(infoResult.error.toDomainError())
            is NetworkResult.Success -> infoResult.data
        }
        val info = infoById[exchangeId.toString()]
            ?: return@coroutineScope DomainResult.Error(DomainError.NotFound)

        val marketPairsResult = marketPairsDeferred.await()
        val marketPairs = (marketPairsResult as? NetworkResult.Success)?.data

        DomainResult.Success(toExchangeDetail(info, marketPairs))
    }
}
