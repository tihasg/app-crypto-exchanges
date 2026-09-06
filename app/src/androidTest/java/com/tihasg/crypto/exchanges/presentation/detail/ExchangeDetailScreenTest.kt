package com.tihasg.crypto.exchanges.presentation.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cryptoexchanges.core.ds.theme.CryptoExchangesTheme
import com.cryptoexchanges.domain.model.Currency
import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.ExchangeDetail
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ExchangeDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleDetail = ExchangeDetail(
        id = 270,
        name = "Binance",
        logoUrl = null,
        description = "Largest crypto exchange",
        websiteUrl = "https://binance.com",
        makerFee = 0.1,
        takerFee = 0.2,
        dateLaunched = "2017-07-14T00:00:00.000Z",
        currencies = listOf(Currency("Bitcoin", 60123.45)),
    )

    @Test
    fun rendersExchangeDetailFields() {
        composeTestRule.setContent {
            CryptoExchangesTheme {
                ExchangeDetailContent(
                    uiState = ExchangeDetailUiState(
                        isLoading = false,
                        exchangeDetail = sampleDetail
                    ),
                    onIntent = {},
                )
            }
        }

        composeTestRule.onNodeWithText("ID: 270").assertIsDisplayed()
        composeTestRule.onNodeWithText("Largest crypto exchange").assertIsDisplayed()
        composeTestRule.onNodeWithText("https://binance.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
    }

    @Test
    fun showsErrorStateWithRetryButton() {
        var retried = false

        composeTestRule.setContent {
            CryptoExchangesTheme {
                ExchangeDetailContent(
                    uiState = ExchangeDetailUiState(
                        isLoading = false,
                        error = DomainError.NotFound
                    ),
                    onIntent = { if (it == ExchangeDetailIntent.OnRetry) retried = true },
                )
            }
        }

        composeTestRule.onNodeWithText("Tentar novamente").performClick()

        assertEquals(true, retried)
    }
}
