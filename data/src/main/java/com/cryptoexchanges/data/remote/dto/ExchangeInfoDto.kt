package com.cryptoexchanges.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeInfoDto(
    val id: Int,
    val name: String,
    val logo: String? = null,
    val description: String? = null,
    @SerialName("date_launched") val dateLaunched: String? = null,
    @SerialName("maker_fee") val makerFee: Double? = null,
    @SerialName("taker_fee") val takerFee: Double? = null,
    val urls: ExchangeUrlsDto? = null,
)

@Serializable
data class ExchangeUrlsDto(
    val website: List<String>? = null,
)
