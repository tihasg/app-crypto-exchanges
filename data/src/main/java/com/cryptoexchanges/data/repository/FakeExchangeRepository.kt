package com.cryptoexchanges.data.repository

import com.cryptoexchanges.domain.model.Currency
import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.model.ExchangeDetail
import com.cryptoexchanges.domain.repository.ExchangeRepository

class FakeExchangeRepository : ExchangeRepository {

    override suspend fun getExchanges(): DomainResult<List<Exchange>> =
        DomainResult.Success(fakeExchanges.map { it.toExchange() })

    override suspend fun getExchangeDetail(exchangeId: Int): DomainResult<ExchangeDetail> {
        val exchange = fakeExchanges.find { it.id == exchangeId }
            ?: return DomainResult.Error(DomainError.NotFound)
        return DomainResult.Success(exchange.toExchangeDetail())
    }

    private data class FakeExchange(
        val id: Int,
        val name: String,
        val logoUrl: String,
        val spotVolumeUsd: Double,
        val dateLaunched: String,
        val description: String,
        val websiteUrl: String,
        val makerFee: Double,
        val takerFee: Double,
        val currencies: List<Currency>,
    )

    private fun FakeExchange.toExchange() = Exchange(
        id = id,
        name = name,
        logoUrl = logoUrl,
        spotVolumeUsd = spotVolumeUsd,
        dateLaunched = dateLaunched,
    )

    private fun FakeExchange.toExchangeDetail() = ExchangeDetail(
        id = id,
        name = name,
        logoUrl = logoUrl,
        description = description,
        websiteUrl = websiteUrl,
        makerFee = makerFee,
        takerFee = takerFee,
        dateLaunched = dateLaunched,
        currencies = currencies,
    )

    private val fakeExchanges = listOf(
        FakeExchange(
            id = 270,
            name = "Binance",
            logoUrl = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/270.png",
            spotVolumeUsd = 12_450_000_000.0,
            dateLaunched = "2017-07-14T00:00:00.000Z",
            description = "Binance is a cryptocurrency exchange that provides a platform for trading more than 500 cryptocurrencies.",
            websiteUrl = "https://www.binance.com",
            makerFee = 0.10,
            takerFee = 0.10,
            currencies = listOf(
                Currency("Bitcoin", 60123.45),
                Currency("Ethereum", 3345.12),
                Currency("Tether", 1.0),
            ),
        ),
        FakeExchange(
            id = 89,
            name = "Coinbase Exchange",
            logoUrl = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/89.png",
            spotVolumeUsd = 2_780_000_000.0,
            dateLaunched = "2012-06-20T00:00:00.000Z",
            description = "Coinbase Exchange is a secure online platform for buying, selling, transferring, and storing cryptocurrency.",
            websiteUrl = "https://www.coinbase.com",
            makerFee = 0.40,
            takerFee = 0.60,
            currencies = listOf(
                Currency("Bitcoin", 60110.02),
                Currency("Ethereum", 3341.87),
                Currency("Solana", 142.30),
            ),
        ),
        FakeExchange(
            id = 24,
            name = "Kraken",
            logoUrl = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/24.png",
            spotVolumeUsd = 980_000_000.0,
            dateLaunched = "2011-07-28T00:00:00.000Z",
            description = "Kraken is a US-based cryptocurrency exchange, founded in 2011.",
            websiteUrl = "https://www.kraken.com",
            makerFee = 0.16,
            takerFee = 0.26,
            currencies = listOf(
                Currency("Bitcoin", 60098.77),
                Currency("Ethereum", 3339.40),
            ),
        ),
        FakeExchange(
            id = 37,
            name = "Bitfinex",
            logoUrl = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/37.png",
            spotVolumeUsd = 410_000_000.0,
            dateLaunched = "2012-12-01T00:00:00.000Z",
            description = "Bitfinex is a full-featured spot trading platform for major cryptocurrencies.",
            websiteUrl = "https://www.bitfinex.com",
            makerFee = 0.10,
            takerFee = 0.20,
            currencies = listOf(
                Currency("Bitcoin", 60089.14),
                Currency("Tether", 0.999),
            ),
        ),
        FakeExchange(
            id = 311,
            name = "KuCoin",
            logoUrl = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/311.png",
            spotVolumeUsd = 1_120_000_000.0,
            dateLaunched = "2017-09-15T00:00:00.000Z",
            description = "KuCoin is a global cryptocurrency exchange for numerous digital assets and cryptocurrencies.",
            websiteUrl = "https://www.kucoin.com",
            makerFee = 0.10,
            takerFee = 0.10,
            currencies = listOf(
                Currency("Bitcoin", 60142.90),
                Currency("KuCoin Token", 12.34),
            ),
        ),
        FakeExchange(
            id = 294,
            name = "OKX",
            logoUrl = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/294.png",
            spotVolumeUsd = 3_050_000_000.0,
            dateLaunched = "2017-05-31T00:00:00.000Z",
            description = "OKX is a technology company that builds crypto-based financial products and services.",
            websiteUrl = "https://www.okx.com",
            makerFee = 0.08,
            takerFee = 0.10,
            currencies = listOf(
                Currency("Bitcoin", 60131.20),
                Currency("Ethereum", 3347.05),
                Currency("OKB", 54.10),
            ),
        ),
    )
}
