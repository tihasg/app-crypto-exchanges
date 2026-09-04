package com.cryptoexchanges.core.network

/** Outcome of a [safeApiCall]. Consumers (typically a `data` repository) map this to their own domain error type. */
sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val error: NetworkError) : NetworkResult<Nothing>
}

/** Low-level network failure reasons, independent of any domain/business error model. */
sealed interface NetworkError {
    data object NoConnectivity : NetworkError
    data object Timeout : NetworkError
    data class Http(val code: Int, val message: String?) : NetworkError
    data object Serialization : NetworkError
    data class Unknown(val message: String?) : NetworkError
}
