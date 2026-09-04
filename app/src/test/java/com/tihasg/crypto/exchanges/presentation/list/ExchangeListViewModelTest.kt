package com.tihasg.crypto.exchanges.presentation.list

import app.cash.turbine.test
import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.usecase.GetExchangesUseCase
import com.tihasg.crypto.exchanges.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getExchangesUseCase = mockk<GetExchangesUseCase>()

    @Test
    fun `starts in loading state and loads exchanges on init`() = runTest(mainDispatcherRule.testDispatcher) {
        val exchanges = listOf(
            Exchange(id = 1, name = "Binance", logoUrl = null, spotVolumeUsd = 100.0, dateLaunched = null),
        )
        coEvery { getExchangesUseCase() } returns DomainResult.Success(exchanges)

        val viewModel = ExchangeListViewModel(getExchangesUseCase)
        assertTrue(viewModel.uiState.value.isLoading)

        advanceUntilIdle()

        assertEquals(
            ExchangeListUiState(isLoading = false, exchanges = exchanges, error = null),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `shows error when use case fails`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangesUseCase() } returns DomainResult.Error(DomainError.NoConnectivity)

        val viewModel = ExchangeListViewModel(getExchangesUseCase)
        advanceUntilIdle()

        assertEquals(
            ExchangeListUiState(isLoading = false, exchanges = emptyList(), error = DomainError.NoConnectivity),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `retry reloads exchanges after a failure`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangesUseCase() } returnsMany listOf(
            DomainResult.Error(DomainError.NoConnectivity),
            DomainResult.Success(emptyList()),
        )

        val viewModel = ExchangeListViewModel(getExchangesUseCase)
        advanceUntilIdle()
        assertEquals(DomainError.NoConnectivity, viewModel.uiState.value.error)

        viewModel.onIntent(ExchangeListIntent.OnRetry)
        advanceUntilIdle()

        assertEquals(
            ExchangeListUiState(isLoading = false, exchanges = emptyList(), error = null),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `exchange click emits navigate effect`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangesUseCase() } returns DomainResult.Success(emptyList())
        val viewModel = ExchangeListViewModel(getExchangesUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onIntent(ExchangeListIntent.OnExchangeClick(42))
            assertEquals(ExchangeListEffect.NavigateToDetail(42), awaitItem())
        }
    }
}
