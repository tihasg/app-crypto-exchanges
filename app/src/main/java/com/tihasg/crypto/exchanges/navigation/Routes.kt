package com.tihasg.crypto.exchanges.navigation

import kotlinx.serialization.Serializable

@Serializable
object ExchangeListRoute

@Serializable
data class ExchangeDetailRoute(val exchangeId: Int)
