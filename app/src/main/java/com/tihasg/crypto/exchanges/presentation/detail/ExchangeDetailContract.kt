package com.tihasg.crypto.exchanges.presentation.detail

import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.ExchangeDetail

data class ExchangeDetailUiState(
    val isLoading: Boolean = true,
    val exchangeDetail: ExchangeDetail? = null,
    val error: DomainError? = null,
)

sealed interface ExchangeDetailIntent {
    data object LoadDetail : ExchangeDetailIntent
    data object OnRetry : ExchangeDetailIntent
    data object OnWebsiteClick : ExchangeDetailIntent
    data object OnBackClick : ExchangeDetailIntent
}

sealed interface ExchangeDetailEffect {
    data class OpenUrl(val url: String) : ExchangeDetailEffect
    data object NavigateBack : ExchangeDetailEffect
}
