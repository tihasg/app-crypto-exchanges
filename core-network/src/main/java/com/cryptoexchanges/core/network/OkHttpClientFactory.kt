package com.cryptoexchanges.core.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClientFactory {
    fun create(
        config: NetworkConfig,
        extraInterceptors: List<Interceptor> = emptyList(),
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(config.connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(config.readTimeoutSeconds, TimeUnit.SECONDS)
            .addInterceptor(ApiKeyInterceptor(config.apiKeyHeaderName, config.apiKey))
            .apply { extraInterceptors.forEach(::addInterceptor) }
            .apply {
                if (config.enableLogging) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY },
                    )
                }
            }
            .build()
    }
}
