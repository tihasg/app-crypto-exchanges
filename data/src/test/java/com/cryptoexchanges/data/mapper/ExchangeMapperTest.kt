package com.cryptoexchanges.data.mapper

import com.cryptoexchanges.data.remote.dto.ExchangeInfoDto
import com.cryptoexchanges.data.remote.dto.ExchangeMapDto
import com.cryptoexchanges.data.remote.dto.ExchangeMarketPairsDto
import com.cryptoexchanges.data.remote.dto.ExchangeUrlsDto
import com.cryptoexchanges.data.remote.dto.MarketPairAssetDto
import com.cryptoexchanges.data.remote.dto.MarketPairDto
import com.cryptoexchanges.data.remote.dto.MarketPairQuoteDto
import com.cryptoexchanges.domain.model.Currency
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ExchangeMapperTest {

    @Test
    fun `maps map and info dtos into an exchange`() {
        val map = ExchangeMapDto(id = 270, name = "Binance", slug = "binance")
        val info = ExchangeInfoDto(
            id = 270,
            name = "Binance",
            logo = "https://logo/270.png",
            dateLaunched = "2017-07-14T00:00:00.000Z",
            spotVolumeUsd = 12_450_000_000.0,
        )

        val exchange = toExchange(map, info)

        assertEquals(270, exchange.id)
        assertEquals("Binance", exchange.name)
        assertEquals("https://logo/270.png", exchange.logoUrl)
        assertEquals(12_450_000_000.0, exchange.spotVolumeUsd)
        assertEquals("2017-07-14T00:00:00.000Z", exchange.dateLaunched)
    }

    @Test
    fun `maps exchange with missing info to nulls`() {
        val map = ExchangeMapDto(id = 1, name = "Unknown")

        val exchange = toExchange(map, info = null)

        assertNull(exchange.logoUrl)
        assertNull(exchange.spotVolumeUsd)
        assertNull(exchange.dateLaunched)
    }

    @Test
    fun `maps info and market pairs into exchange detail`() {
        val info = ExchangeInfoDto(
            id = 270,
            name = "Binance",
            logo = "https://logo/270.png",
            description = "Largest exchange",
            dateLaunched = "2017-07-14T00:00:00.000Z",
            makerFee = 0.1,
            takerFee = 0.1,
            urls = ExchangeUrlsDto(website = listOf("https://binance.com")),
        )
        val marketPairs = ExchangeMarketPairsDto(
            id = 270,
            name = "Binance",
            marketPairs = listOf(
                MarketPairDto(
                    base = MarketPairAssetDto(currencyName = "Bitcoin", currencySymbol = "BTC"),
                    quote = mapOf("USD" to MarketPairQuoteDto(price = 60123.45)),
                ),
                MarketPairDto(
                    base = MarketPairAssetDto(currencyName = "Ethereum", currencySymbol = "ETH"),
                    quote = mapOf("USD" to MarketPairQuoteDto(price = 3345.12)),
                ),
            ),
        )

        val detail = toExchangeDetail(info, marketPairs)

        assertEquals(270, detail.id)
        assertEquals("https://binance.com", detail.websiteUrl)
        assertEquals(0.1, detail.makerFee)
        assertEquals(
            listOf(Currency("Bitcoin", 60123.45), Currency("Ethereum", 3345.12)),
            detail.currencies,
        )
    }

    @Test
    fun `deduplicates currencies by name and skips pairs without a currency name`() {
        val info = ExchangeInfoDto(id = 270, name = "Binance")
        val marketPairs = ExchangeMarketPairsDto(
            id = 270,
            name = "Binance",
            marketPairs = listOf(
                MarketPairDto(
                    base = MarketPairAssetDto(currencyName = "Bitcoin"),
                    quote = mapOf("USD" to MarketPairQuoteDto(price = 60123.45)),
                ),
                MarketPairDto(
                    base = MarketPairAssetDto(currencyName = "Bitcoin"),
                    quote = mapOf("USD" to MarketPairQuoteDto(price = 60125.00)),
                ),
                MarketPairDto(base = null, quote = null),
            ),
        )

        val detail = toExchangeDetail(info, marketPairs)

        assertEquals(1, detail.currencies.size)
        assertEquals("Bitcoin", detail.currencies.first().name)
    }

    @Test
    fun `maps exchange detail with null market pairs to empty currencies`() {
        val info = ExchangeInfoDto(id = 270, name = "Binance")

        val detail = toExchangeDetail(info, marketPairs = null)

        assertEquals(emptyList<Currency>(), detail.currencies)
    }

    @Test
    fun `rejects non-https website urls to prevent arbitrary intent launches`() {
        val info = ExchangeInfoDto(
            id = 270,
            name = "Binance",
            urls = ExchangeUrlsDto(website = listOf("javascript:alert(1)")),
        )

        val detail = toExchangeDetail(info, marketPairs = null)

        assertNull(detail.websiteUrl)
    }

    @Test
    fun `falls back to the next website url when the first one is unsafe`() {
        val info = ExchangeInfoDto(
            id = 270,
            name = "Binance",
            urls = ExchangeUrlsDto(website = listOf("http://binance.com", "https://binance.com")),
        )

        val detail = toExchangeDetail(info, marketPairs = null)

        assertEquals("https://binance.com", detail.websiteUrl)
    }
}
