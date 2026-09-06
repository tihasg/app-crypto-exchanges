package com.cryptoexchanges.data.mapper

import com.cryptoexchanges.core.network.NetworkError
import com.cryptoexchanges.domain.model.DomainError

internal fun NetworkError.toDomainError(): DomainError = when (this) {
    NetworkError.NoConnectivity -> DomainError.NoConnectivity
    NetworkError.Timeout -> DomainError.Timeout
    NetworkError.Serialization -> DomainError.Parsing
    is NetworkError.Http -> when (code) {
        401, 403 -> DomainError.Unauthorized
        404 -> DomainError.NotFound
        else -> DomainError.ServerError(code)
    }

    is NetworkError.Unknown -> DomainError.Unknown(message)
}
