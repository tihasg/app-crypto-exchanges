package com.cryptoexchanges.data.mapper

import com.cryptoexchanges.data.remote.dto.ExchangeInfoDto
import com.cryptoexchanges.data.remote.dto.ExchangeMapDto
import com.cryptoexchanges.data.remote.dto.ExchangeMarketPairsDto
import com.cryptoexchanges.data.remote.dto.MarketPairDto
import com.cryptoexchanges.domain.model.Currency
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.model.ExchangeDetail

fun toExchange(map: ExchangeMapDto, info: ExchangeInfoDto?): Exchange {
    return Exchange(
        id = map.id,
        name = map.name,
        logoUrl = info?.logo,
        spotVolumeUsd = info?.spotVolumeUsd,
        dateLaunched = info?.dateLaunched,
    )
}

fun toExchangeDetail(info: ExchangeInfoDto, marketPairs: ExchangeMarketPairsDto?): ExchangeDetail {
    return ExchangeDetail(
        id = info.id,
        name = info.name,
        logoUrl = info.logo,
        description = info.description,
        websiteUrl = info.urls?.website?.firstOrNull(),
        makerFee = info.makerFee,
        takerFee = info.takerFee,
        dateLaunched = info.dateLaunched,
        currencies = marketPairs?.marketPairs.orEmpty().mapNotNull { it.toCurrency() }.distinctBy { it.name },
    )
}

private fun MarketPairDto.toCurrency(): Currency? {
    val name = base?.currencyName ?: base?.currencySymbol ?: return null
    return Currency(name = name, priceUsd = quote?.get("USD")?.price)
}
