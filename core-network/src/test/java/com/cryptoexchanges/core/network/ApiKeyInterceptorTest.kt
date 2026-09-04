package com.cryptoexchanges.core.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ApiKeyInterceptorTest {

    private val server = MockWebServer()

    @Before
    fun setUp() {
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `adds api key header to every request`() {
        server.enqueue(MockResponse().setBody("{}"))
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(headerName = "X-CMC_PRO_API_KEY", apiKey = "test-key"))
            .build()

        client.newCall(Request.Builder().url(server.url("/v1/exchange/map")).build()).execute()

        val recorded = server.takeRequest()
        assertEquals("test-key", recorded.getHeader("X-CMC_PRO_API_KEY"))
    }
}
