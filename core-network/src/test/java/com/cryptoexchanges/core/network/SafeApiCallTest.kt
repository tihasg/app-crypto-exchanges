package com.cryptoexchanges.core.network

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SafeApiCallTest {

    @Test
    fun `returns success when call completes`() = runTest {
        val result = safeApiCall { "ok" }

        assertEquals(NetworkResult.Success("ok"), result)
    }

    @Test
    fun `maps SocketTimeoutException to Timeout`() = runTest {
        val result = safeApiCall<Unit> { throw SocketTimeoutException() }

        assertEquals(NetworkResult.Error(NetworkError.Timeout), result)
    }

    @Test
    fun `maps UnknownHostException to NoConnectivity`() = runTest {
        val result = safeApiCall<Unit> { throw UnknownHostException() }

        assertEquals(NetworkResult.Error(NetworkError.NoConnectivity), result)
    }

    @Test
    fun `maps generic IOException to NoConnectivity`() = runTest {
        val result = safeApiCall<Unit> { throw IOException("boom") }

        assertEquals(NetworkResult.Error(NetworkError.NoConnectivity), result)
    }

    @Test
    fun `maps HttpException to Http with code`() = runTest {
        val response = Response.error<Unit>(
            404,
            "not found".toResponseBody("text/plain".toMediaType()),
        )

        val result = safeApiCall<Unit> { throw HttpException(response) }

        assertEquals(NetworkResult.Error(NetworkError.Http(404, "Response.error()")), result)
    }

    @Test
    fun `maps SerializationException to Serialization`() = runTest {
        val result = safeApiCall<Unit> { throw SerializationException("bad json") }

        assertEquals(NetworkResult.Error(NetworkError.Serialization), result)
    }

    @Test
    fun `maps unexpected exception to Unknown`() = runTest {
        val result = safeApiCall<Unit> { throw IllegalStateException("weird") }

        assertEquals(NetworkResult.Error(NetworkError.Unknown("weird")), result)
    }
}
