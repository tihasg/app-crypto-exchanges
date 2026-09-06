package com.tihasg.crypto.exchanges.presentation.list

import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.Exchange

data class ExchangeListUiState(
    val isLoading: Boolean = true,
    val exchanges: List<Exchange> = emptyList(),
    val searchQuery: String = "",
    val error: DomainError? = null,
)

sealed interface ExchangeListIntent {
    data object LoadExchanges : ExchangeListIntent
    data object OnRetry : ExchangeListIntent
    data class OnExchangeClick(val exchangeId: Int) : ExchangeListIntent
    data class OnSearchQueryChange(val query: String) : ExchangeListIntent
}

sealed interface ExchangeListEffect {
    data class NavigateToDetail(val exchangeId: Int) : ExchangeListEffect
}
