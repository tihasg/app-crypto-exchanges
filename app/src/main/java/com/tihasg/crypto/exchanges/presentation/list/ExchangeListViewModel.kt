package com.tihasg.crypto.exchanges.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.usecase.GetExchangesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExchangeListViewModel(
    private val getExchangesUseCase: GetExchangesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeListUiState())
    val uiState: StateFlow<ExchangeListUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ExchangeListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadExchanges()
    }

    fun onIntent(intent: ExchangeListIntent) {
        when (intent) {
            ExchangeListIntent.LoadExchanges, ExchangeListIntent.OnRetry -> loadExchanges()
            is ExchangeListIntent.OnExchangeClick -> viewModelScope.launch {
                _effect.send(ExchangeListEffect.NavigateToDetail(intent.exchangeId))
            }
        }
    }

    private fun loadExchanges() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getExchangesUseCase()) {
                is DomainResult.Success -> _uiState.update {
                    it.copy(isLoading = false, exchanges = result.data, error = null)
                }
                is DomainResult.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.error)
                }
            }
        }
    }
}
