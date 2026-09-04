package com.cryptoexchanges.data.remote

import com.cryptoexchanges.data.remote.dto.CmcResponseDto
import com.cryptoexchanges.data.remote.dto.ExchangeInfoDto
import com.cryptoexchanges.data.remote.dto.ExchangeMapDto
import com.cryptoexchanges.data.remote.dto.ExchangeMarketPairsDto
import com.cryptoexchanges.data.remote.dto.ExchangeQuoteDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeApiService {

    @GET("v1/exchange/map")
    suspend fun getExchangeMap(
        @Query("listing_status") listingStatus: String = "active",
        @Query("limit") limit: Int = 50,
    ): CmcResponseDto<List<ExchangeMapDto>>

    @GET("v1/exchange/info")
    suspend fun getExchangeInfo(@Query("id") ids: String): CmcResponseDto<Map<String, ExchangeInfoDto>>

    @GET("v1/exchange/quotes/latest")
    suspend fun getExchangeQuotes(@Query("id") ids: String): CmcResponseDto<Map<String, ExchangeQuoteDto>>

    @GET("v1/exchange/market-pairs/latest")
    suspend fun getExchangeMarketPairs(@Query("id") id: Int): CmcResponseDto<ExchangeMarketPairsDto>
}
