package com.cryptoexchanges.core.network

data class NetworkConfig(
    val baseUrl: String,
    val apiKey: String,
    val apiKeyHeaderName: String = "X-CMC_PRO_API_KEY",
    val enableLogging: Boolean = false,
    val connectTimeoutSeconds: Long = 30,
    val readTimeoutSeconds: Long = 30,
)
