package com.tihasg.crypto.exchanges.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.usecase.GetExchangeDetailUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExchangeDetailViewModel(
    private val exchangeId: Int,
    private val getExchangeDetailUseCase: GetExchangeDetailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeDetailUiState())
    val uiState: StateFlow<ExchangeDetailUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ExchangeDetailEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadDetail()
    }

    fun onIntent(intent: ExchangeDetailIntent) {
        when (intent) {
            ExchangeDetailIntent.LoadDetail, ExchangeDetailIntent.OnRetry -> loadDetail()
            ExchangeDetailIntent.OnWebsiteClick -> {
                val url = _uiState.value.exchangeDetail?.websiteUrl
                if (!url.isNullOrBlank()) {
                    viewModelScope.launch { _effect.send(ExchangeDetailEffect.OpenUrl(url)) }
                }
            }
            ExchangeDetailIntent.OnBackClick -> viewModelScope.launch {
                _effect.send(ExchangeDetailEffect.NavigateBack)
            }
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getExchangeDetailUseCase(exchangeId)) {
                is DomainResult.Success -> _uiState.update {
                    it.copy(isLoading = false, exchangeDetail = result.data, error = null)
                }
                is DomainResult.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.error)
                }
            }
        }
    }
}
