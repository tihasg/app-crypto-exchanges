package com.cryptoexchanges.core.network

/**
 * Configuration for [OkHttpClientFactory]/[RetrofitFactory]. The API key and base URL are
 * supplied by the consuming app (e.g. from `BuildConfig`) — this module never reads them
 * itself, keeping it free of any Android/Hilt coupling.
 */
data class NetworkConfig(
    val baseUrl: String,
    val apiKey: String,
    val apiKeyHeaderName: String = "X-CMC_PRO_API_KEY",
    val enableLogging: Boolean = false,
    val connectTimeoutSeconds: Long = 30,
    val readTimeoutSeconds: Long = 30,
)
