package com.cryptoexchanges.domain.model

sealed interface DomainError {
    data object NoConnectivity : DomainError
    data object Timeout : DomainError
    data object Unauthorized : DomainError
    data object NotFound : DomainError
    data class ServerError(val code: Int) : DomainError
    data object Parsing : DomainError
    data class Unknown(val message: String?) : DomainError
}
