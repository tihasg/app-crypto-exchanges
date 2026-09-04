package com.cryptoexchanges.data.remote

import com.cryptoexchanges.core.network.NetworkError
import com.cryptoexchanges.core.network.NetworkResult
import com.cryptoexchanges.data.remote.dto.CmcResponseDto
import com.cryptoexchanges.data.remote.dto.CmcStatusDto
import com.cryptoexchanges.data.remote.dto.ExchangeMapDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExchangeRemoteDataSourceTest {

    private val api = mockk<ExchangeApiService>()
    private val dataSource = ExchangeRemoteDataSource(api)

    @Test
    fun `returns success when the response has data`() = runTest {
        val exchanges = listOf(ExchangeMapDto(id = 270, name = "Binance"))
        coEvery { api.getExchangeMap(limit = 50) } returns CmcResponseDto(
            status = CmcStatusDto(errorCode = 0),
            data = exchanges,
        )

        val result = dataSource.getExchangeMap()

        assertEquals(NetworkResult.Success(exchanges), result)
    }

    @Test
    fun `maps a null data payload to an Unknown network error`() = runTest {
        coEvery { api.getExchangeMap(limit = 50) } returns CmcResponseDto(
            status = CmcStatusDto(errorCode = 1006, errorMessage = "Plan requires upgrade"),
            data = null,
        )

        val result = dataSource.getExchangeMap()

        assertTrue(result is NetworkResult.Error)
        val error = (result as NetworkResult.Error).error
        assertTrue(error is NetworkError.Unknown)
        assertEquals("Plan requires upgrade", (error as NetworkError.Unknown).message)
    }
}
