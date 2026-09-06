package com.tihasg.crypto.exchanges.presentation.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.cryptoexchanges.core.ds.theme.CryptoExchangesTheme
import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.Exchange
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ExchangeListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleExchange = Exchange(
        id = 270,
        name = "Binance",
        logoUrl = null,
        spotVolumeUsd = 12_450_000_000.0,
        dateLaunched = "2017-07-14T00:00:00.000Z",
    )
    private val otherExchange = Exchange(
        id = 89,
        name = "Coinbase",
        logoUrl = null,
        spotVolumeUsd = 5_000_000_000.0,
        dateLaunched = "2012-06-20T00:00:00.000Z",
    )

    @Test
    fun rendersExchangeItem() {
        composeTestRule.setContent {
            CryptoExchangesTheme {
                ExchangeListContent(
                    uiState = ExchangeListUiState(
                        isLoading = false,
                        exchanges = listOf(sampleExchange)
                    ),
                    onIntent = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Binance").assertIsDisplayed()
    }

    @Test
    fun clickingItemEmitsExchangeClickIntent() {
        var capturedIntent: ExchangeListIntent? = null

        composeTestRule.setContent {
            CryptoExchangesTheme {
                ExchangeListContent(
                    uiState = ExchangeListUiState(
                        isLoading = false,
                        exchanges = listOf(sampleExchange)
                    ),
                    onIntent = { capturedIntent = it },
                )
            }
        }

        composeTestRule.onNodeWithText("Binance").performClick()

        assertEquals(ExchangeListIntent.OnExchangeClick(270), capturedIntent)
    }

    @Test
    fun showsErrorStateWithRetryButton() {
        var retried = false

        composeTestRule.setContent {
            CryptoExchangesTheme {
                ExchangeListContent(
                    uiState = ExchangeListUiState(
                        isLoading = false,
                        error = DomainError.NoConnectivity
                    ),
                    onIntent = { if (it == ExchangeListIntent.OnRetry) retried = true },
                )
            }
        }

        composeTestRule.onNodeWithText("Tentar novamente").performClick()

        assertEquals(true, retried)
    }

    @Test
    fun typingInSearchFieldEmitsSearchQueryIntent() {
        var capturedIntent: ExchangeListIntent? = null

        composeTestRule.setContent {
            CryptoExchangesTheme {
                ExchangeListContent(
                    uiState = ExchangeListUiState(isLoading = false, exchanges = listOf(sampleExchange, otherExchange)),
                    onIntent = { capturedIntent = it },
                )
            }
        }

        composeTestRule.onNodeWithText("Buscar exchange").performTextReplacement("bin")

        assertEquals(ExchangeListIntent.OnSearchQueryChange("bin"), capturedIntent)
    }

    @Test
    fun showsEmptyStateWithQueryWhenSearchHasNoMatches() {
        composeTestRule.setContent {
            CryptoExchangesTheme {
                ExchangeListContent(
                    uiState = ExchangeListUiState(isLoading = false, exchanges = emptyList(), searchQuery = "xyz"),
                    onIntent = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Nenhuma exchange encontrada para \"xyz\"").assertIsDisplayed()
    }
}
