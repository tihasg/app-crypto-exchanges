package com.tihasg.crypto.exchanges.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.usecase.GetExchangesUseCase
import com.cryptoexchanges.domain.usecase.ObserveExchangesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExchangeListViewModel(
    private val getExchangesUseCase: GetExchangesUseCase,
    private val observeExchangesUseCase: ObserveExchangesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeListUiState())
    val uiState: StateFlow<ExchangeListUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ExchangeListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var allExchanges: List<Exchange> = emptyList()

    init {
        loadExchanges(showLoading = true)
        observeCachedExchanges()
    }

    fun onIntent(intent: ExchangeListIntent) {
        when (intent) {
            ExchangeListIntent.LoadExchanges, ExchangeListIntent.OnRetry -> loadExchanges(
                showLoading = true
            )

            is ExchangeListIntent.OnExchangeClick -> viewModelScope.launch {
                _effect.send(ExchangeListEffect.NavigateToDetail(intent.exchangeId))
            }

            is ExchangeListIntent.OnSearchQueryChange -> onSearchQueryChange(intent.query)
        }
    }

    private fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                exchanges = allExchanges.filterByQuery(query)
            )
        }
    }

    private fun observeCachedExchanges() {
        viewModelScope.launch {
            observeExchangesUseCase().collect { cached ->
                if (cached.isNotEmpty()) applyExchanges(cached)
            }
        }
    }

    private fun loadExchanges(showLoading: Boolean) {
        viewModelScope.launch {
            if (showLoading) _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getExchangesUseCase()) {
                is DomainResult.Success -> applyExchanges(result.data)
                is DomainResult.Error -> _uiState.update {
                    if (showLoading || allExchanges.isEmpty()) {
                        it.copy(isLoading = false, error = result.error)
                    } else {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun applyExchanges(exchanges: List<Exchange>) {
        allExchanges = exchanges
        _uiState.update {
            it.copy(
                isLoading = false,
                exchanges = allExchanges.filterByQuery(it.searchQuery),
                error = null
            )
        }
    }

    private fun List<Exchange>.filterByQuery(query: String): List<Exchange> =
        if (query.isBlank()) this else filter { it.name.contains(query, ignoreCase = true) }
}
