package com.cryptoexchanges.data.mapper

import com.cryptoexchanges.data.local.ExchangeCacheDto
import com.cryptoexchanges.data.remote.dto.ExchangeInfoDto
import com.cryptoexchanges.data.remote.dto.ExchangeMapDto
import com.cryptoexchanges.data.remote.dto.ExchangeMarketPairsDto
import com.cryptoexchanges.data.remote.dto.MarketPairDto
import com.cryptoexchanges.domain.model.Currency
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.model.ExchangeDetail
import java.net.URI

fun toExchange(map: ExchangeMapDto, info: ExchangeInfoDto?): Exchange {
    return Exchange(
        id = map.id,
        name = map.name,
        logoUrl = info?.logo,
        spotVolumeUsd = info?.spotVolumeUsd,
        dateLaunched = info?.dateLaunched,
    )
}

fun Exchange.toCacheDto(): ExchangeCacheDto = ExchangeCacheDto(
    id = id,
    name = name,
    logoUrl = logoUrl,
    spotVolumeUsd = spotVolumeUsd,
    dateLaunched = dateLaunched,
)

fun ExchangeCacheDto.toDomain(): Exchange = Exchange(
    id = id,
    name = name,
    logoUrl = logoUrl,
    spotVolumeUsd = spotVolumeUsd,
    dateLaunched = dateLaunched,
)

fun toExchangeDetail(info: ExchangeInfoDto, marketPairs: ExchangeMarketPairsDto?): ExchangeDetail {
    return ExchangeDetail(
        id = info.id,
        name = info.name,
        logoUrl = info.logo,
        description = info.description,
        websiteUrl = info.urls?.website?.firstOrNull { it.isSafeHttpsUrl() },
        makerFee = info.makerFee,
        takerFee = info.takerFee,
        dateLaunched = info.dateLaunched,
        currencies = marketPairs?.marketPairs.orEmpty().mapNotNull { it.toCurrency() }
            .distinctBy { it.name },
    )
}

private fun String.isSafeHttpsUrl(): Boolean {
    val uri = runCatching { URI(this) }.getOrNull() ?: return false
    return uri.scheme.equals("https", ignoreCase = true) && !uri.host.isNullOrBlank()
}

private fun MarketPairDto.toCurrency(): Currency? {
    val name = base?.currencyName ?: base?.currencySymbol ?: return null
    return Currency(name = name, priceUsd = quote?.get("USD")?.price)
}
