package com.cryptoexchanges.data.repository

import com.cryptoexchanges.core.network.NetworkResult
import com.cryptoexchanges.data.local.ExchangeLocalDataSource
import com.cryptoexchanges.data.mapper.toCacheDto
import com.cryptoexchanges.data.mapper.toDomain
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExchangeRepositoryImpl(
    private val remoteDataSource: ExchangeRemoteDataSource,
    private val localDataSource: ExchangeLocalDataSource,
) : ExchangeRepository {

    override suspend fun getExchanges(): DomainResult<List<Exchange>> = coroutineScope {
        val mapResult = remoteDataSource.getExchangeMap()
        val exchangeMaps = when (mapResult) {
            is NetworkResult.Error -> return@coroutineScope DomainResult.Error(mapResult.error.toDomainError())
            is NetworkResult.Success -> mapResult.data
        }
        if (exchangeMaps.isEmpty()) return@coroutineScope DomainResult.Success(emptyList())

        val ids = exchangeMaps.map { it.id }
        val infoResult = remoteDataSource.getExchangeInfo(ids)
        val infoById = when (infoResult) {
            is NetworkResult.Error -> return@coroutineScope DomainResult.Error(infoResult.error.toDomainError())
            is NetworkResult.Success -> infoResult.data
        }

        val exchanges = exchangeMaps.map { map ->
            toExchange(map, infoById[map.id.toString()])
        }
        localDataSource.saveExchanges(exchanges.map { it.toCacheDto() })
        DomainResult.Success(exchanges)
    }

    override fun observeCachedExchanges(): Flow<List<Exchange>> =
        localDataSource.observeExchanges().map { cached -> cached.map { it.toDomain() } }

    override suspend fun getExchangeDetail(exchangeId: Int): DomainResult<ExchangeDetail> =
        coroutineScope {
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
