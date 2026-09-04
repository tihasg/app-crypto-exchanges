package com.cryptoexchanges.data.remote

import com.cryptoexchanges.core.network.NetworkResult
import com.cryptoexchanges.core.network.safeApiCall
import com.cryptoexchanges.data.remote.dto.CmcResponseDto
import com.cryptoexchanges.data.remote.dto.ExchangeInfoDto
import com.cryptoexchanges.data.remote.dto.ExchangeMapDto
import com.cryptoexchanges.data.remote.dto.ExchangeMarketPairsDto
import com.cryptoexchanges.data.remote.dto.ExchangeQuoteDto

class ExchangeRemoteDataSource(private val api: ExchangeApiService) {

    suspend fun getExchangeMap(limit: Int = 50): NetworkResult<List<ExchangeMapDto>> =
        safeApiCall { api.getExchangeMap(limit = limit).dataOrThrow() }

    suspend fun getExchangeInfo(ids: List<Int>): NetworkResult<Map<String, ExchangeInfoDto>> =
        safeApiCall { api.getExchangeInfo(ids.joinToString(",")).dataOrThrow() }

    suspend fun getExchangeQuotes(ids: List<Int>): NetworkResult<Map<String, ExchangeQuoteDto>> =
        safeApiCall { api.getExchangeQuotes(ids.joinToString(",")).dataOrThrow() }

    suspend fun getExchangeMarketPairs(id: Int): NetworkResult<ExchangeMarketPairsDto> =
        safeApiCall { api.getExchangeMarketPairs(id).dataOrThrow() }
}

private fun <T> CmcResponseDto<T>.dataOrThrow(): T =
    data ?: throw CmcApiException(status.errorCode, status.errorMessage)
