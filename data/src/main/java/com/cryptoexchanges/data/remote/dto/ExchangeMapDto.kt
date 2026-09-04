package com.cryptoexchanges.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeMapDto(
    val id: Int,
    val name: String,
    val slug: String? = null,
    @SerialName("is_active") val isActive: Int? = null,
)
