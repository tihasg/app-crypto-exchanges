package com.cryptoexchanges.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CmcResponseDto<T>(
    val status: CmcStatusDto,
    val data: T? = null,
)

@Serializable
data class CmcStatusDto(
    @SerialName("error_code") val errorCode: Int,
    @SerialName("error_message") val errorMessage: String? = null,
)
