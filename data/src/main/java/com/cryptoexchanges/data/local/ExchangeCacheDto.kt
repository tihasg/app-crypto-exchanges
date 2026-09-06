package com.cryptoexchanges.data.local

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeCacheDto(
    val id: Int,
    val name: String,
    val logoUrl: String?,
    val spotVolumeUsd: Double?,
    val dateLaunched: String?,
)
