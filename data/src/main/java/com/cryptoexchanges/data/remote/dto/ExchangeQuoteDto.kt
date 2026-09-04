package com.cryptoexchanges.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeQuoteDto(
    val id: Int,
    val name: String,
    val quote: Map<String, ExchangeQuoteUsdDto>? = null,
)

@Serializable
data class ExchangeQuoteUsdDto(
    @SerialName("spot_volume_usd") val spotVolumeUsd: Double? = null,
)
