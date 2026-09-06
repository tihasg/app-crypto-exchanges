package com.tihasg.crypto.exchanges.presentation.list

import app.cash.turbine.test
import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.usecase.GetExchangesUseCase
import com.cryptoexchanges.domain.usecase.ObserveExchangesUseCase
import com.tihasg.crypto.exchanges.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runCurrent
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
    private val observeExchangesUseCase = mockk<ObserveExchangesUseCase>()

    private val binance = Exchange(
        id = 1,
        name = "Binance",
        logoUrl = null,
        spotVolumeUsd = 100.0,
        dateLaunched = null
    )
    private val coinbase = Exchange(
        id = 2,
        name = "Coinbase",
        logoUrl = null,
        spotVolumeUsd = 50.0,
        dateLaunched = null
    )
    private val kraken =
        Exchange(id = 3, name = "Kraken", logoUrl = null, spotVolumeUsd = 30.0, dateLaunched = null)

    private fun viewModel() = ExchangeListViewModel(getExchangesUseCase, observeExchangesUseCase)

    @Test
    fun `starts in loading state and loads exchanges on init`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val exchanges = listOf(binance)
            coEvery { getExchangesUseCase() } returns DomainResult.Success(exchanges)
            every { observeExchangesUseCase() } returns emptyFlow()

            val viewModel = viewModel()
            assertTrue(viewModel.uiState.value.isLoading)

            runCurrent()

            assertEquals(
                ExchangeListUiState(isLoading = false, exchanges = exchanges, error = null),
                viewModel.uiState.value,
            )
        }

    @Test
    fun `shows error when use case fails`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangesUseCase() } returns DomainResult.Error(DomainError.NoConnectivity)
        every { observeExchangesUseCase() } returns emptyFlow()

        val viewModel = viewModel()
        runCurrent()

        assertEquals(
            ExchangeListUiState(
                isLoading = false,
                exchanges = emptyList(),
                error = DomainError.NoConnectivity
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `retry reloads exchanges after a failure`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangesUseCase() } returnsMany listOf(
            DomainResult.Error(DomainError.NoConnectivity),
            DomainResult.Success(emptyList()),
        )
        every { observeExchangesUseCase() } returns emptyFlow()

        val viewModel = viewModel()
        runCurrent()
        assertEquals(DomainError.NoConnectivity, viewModel.uiState.value.error)

        viewModel.onIntent(ExchangeListIntent.OnRetry)
        runCurrent()

        assertEquals(
            ExchangeListUiState(isLoading = false, exchanges = emptyList(), error = null),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `exchange click emits navigate effect`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangesUseCase() } returns DomainResult.Success(emptyList())
        every { observeExchangesUseCase() } returns emptyFlow()
        val viewModel = viewModel()
        runCurrent()

        viewModel.effect.test {
            viewModel.onIntent(ExchangeListIntent.OnExchangeClick(42))
            assertEquals(ExchangeListEffect.NavigateToDetail(42), awaitItem())
        }
    }

    @Test
    fun `search filters exchanges by partial case-insensitive name`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery { getExchangesUseCase() } returns DomainResult.Success(
                listOf(
                    binance,
                    coinbase,
                    kraken
                )
            )
            every { observeExchangesUseCase() } returns emptyFlow()
            val viewModel = viewModel()
            runCurrent()

            viewModel.onIntent(ExchangeListIntent.OnSearchQueryChange("bin"))

            assertEquals(listOf(binance), viewModel.uiState.value.exchanges)
            assertEquals("bin", viewModel.uiState.value.searchQuery)
        }

    @Test
    fun `clearing search query restores the full list`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery { getExchangesUseCase() } returns DomainResult.Success(
                listOf(
                    binance,
                    coinbase
                )
            )
            every { observeExchangesUseCase() } returns emptyFlow()
            val viewModel = viewModel()
            runCurrent()

            viewModel.onIntent(ExchangeListIntent.OnSearchQueryChange("coin"))
            viewModel.onIntent(ExchangeListIntent.OnSearchQueryChange(""))

            assertEquals(listOf(binance, coinbase), viewModel.uiState.value.exchanges)
        }

    @Test
    fun `background sync update from the cache refreshes the visible list`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery { getExchangesUseCase() } returns DomainResult.Success(listOf(binance))
            val cacheUpdates = MutableSharedFlow<List<Exchange>>(replay = 1)
            every { observeExchangesUseCase() } returns cacheUpdates

            val viewModel = viewModel()
            runCurrent()
            assertEquals(listOf(binance), viewModel.uiState.value.exchanges)

            cacheUpdates.emit(listOf(binance, coinbase))
            runCurrent()

            assertEquals(listOf(binance, coinbase), viewModel.uiState.value.exchanges)
            assertEquals(false, viewModel.uiState.value.isLoading)
        }

    @Test
    fun `background sync update keeps respecting the active search query`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery { getExchangesUseCase() } returns DomainResult.Success(
                listOf(
                    binance,
                    coinbase
                )
            )
            val cacheUpdates = MutableSharedFlow<List<Exchange>>(replay = 1)
            every { observeExchangesUseCase() } returns cacheUpdates

            val viewModel = viewModel()
            runCurrent()
            viewModel.onIntent(ExchangeListIntent.OnSearchQueryChange("bin"))

            cacheUpdates.emit(listOf(binance, coinbase, kraken))
            runCurrent()

            assertEquals(listOf(binance), viewModel.uiState.value.exchanges)
        }
}
