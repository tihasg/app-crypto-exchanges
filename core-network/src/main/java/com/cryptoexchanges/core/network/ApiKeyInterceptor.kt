package com.cryptoexchanges.core.network

import okhttp3.Interceptor
import okhttp3.Response

/** Adds the CoinMarketCap API key header to every outgoing request. */
class ApiKeyInterceptor(
    private val headerName: String,
    private val apiKey: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(headerName, apiKey)
            .build()
        return chain.proceed(request)
    }
}
