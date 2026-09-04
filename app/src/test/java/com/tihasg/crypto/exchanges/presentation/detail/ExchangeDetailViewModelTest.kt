package com.tihasg.crypto.exchanges.presentation.detail

import app.cash.turbine.test
import com.cryptoexchanges.domain.model.Currency
import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.ExchangeDetail
import com.cryptoexchanges.domain.usecase.GetExchangeDetailUseCase
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
class ExchangeDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getExchangeDetailUseCase = mockk<GetExchangeDetailUseCase>()

    private val sampleDetail = ExchangeDetail(
        id = 270,
        name = "Binance",
        logoUrl = "logo.png",
        description = "Largest exchange",
        websiteUrl = "https://binance.com",
        makerFee = 0.1,
        takerFee = 0.1,
        dateLaunched = "2017-07-14T00:00:00.000Z",
        currencies = listOf(Currency("Bitcoin", 60000.0)),
    )

    @Test
    fun `starts in loading state and loads detail on init`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangeDetailUseCase(270) } returns DomainResult.Success(sampleDetail)

        val viewModel = ExchangeDetailViewModel(270, getExchangeDetailUseCase)
        assertTrue(viewModel.uiState.value.isLoading)

        advanceUntilIdle()

        assertEquals(
            ExchangeDetailUiState(isLoading = false, exchangeDetail = sampleDetail, error = null),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `shows error when use case fails`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangeDetailUseCase(999) } returns DomainResult.Error(DomainError.NotFound)

        val viewModel = ExchangeDetailViewModel(999, getExchangeDetailUseCase)
        advanceUntilIdle()

        assertEquals(DomainError.NotFound, viewModel.uiState.value.error)
    }

    @Test
    fun `retry reloads detail after a failure`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangeDetailUseCase(270) } returnsMany listOf(
            DomainResult.Error(DomainError.Timeout),
            DomainResult.Success(sampleDetail),
        )

        val viewModel = ExchangeDetailViewModel(270, getExchangeDetailUseCase)
        advanceUntilIdle()
        assertEquals(DomainError.Timeout, viewModel.uiState.value.error)

        viewModel.onIntent(ExchangeDetailIntent.OnRetry)
        advanceUntilIdle()

        assertEquals(sampleDetail, viewModel.uiState.value.exchangeDetail)
    }

    @Test
    fun `website click emits open url effect`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangeDetailUseCase(270) } returns DomainResult.Success(sampleDetail)
        val viewModel = ExchangeDetailViewModel(270, getExchangeDetailUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onIntent(ExchangeDetailIntent.OnWebsiteClick)
            assertEquals(ExchangeDetailEffect.OpenUrl("https://binance.com"), awaitItem())
        }
    }

    @Test
    fun `back click emits navigate back effect`() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { getExchangeDetailUseCase(270) } returns DomainResult.Success(sampleDetail)
        val viewModel = ExchangeDetailViewModel(270, getExchangeDetailUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onIntent(ExchangeDetailIntent.OnBackClick)
            assertEquals(ExchangeDetailEffect.NavigateBack, awaitItem())
        }
    }
}
