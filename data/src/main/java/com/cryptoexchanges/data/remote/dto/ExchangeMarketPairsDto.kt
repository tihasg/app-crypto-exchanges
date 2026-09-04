package com.cryptoexchanges.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeMarketPairsDto(
    val id: Int,
    val name: String,
    @SerialName("market_pairs") val marketPairs: List<MarketPairDto> = emptyList(),
)

@Serializable
data class MarketPairDto(
    @SerialName("market_pair_base") val base: MarketPairAssetDto? = null,
    val quote: Map<String, MarketPairQuoteDto>? = null,
)

@Serializable
data class MarketPairAssetDto(
    @SerialName("currency_name") val currencyName: String? = null,
    @SerialName("currency_symbol") val currencySymbol: String? = null,
)

@Serializable
data class MarketPairQuoteDto(
    val price: Double? = null,
)
