package com.cryptoexchanges.data.repository

import com.cryptoexchanges.core.network.NetworkError
import com.cryptoexchanges.core.network.NetworkResult
import com.cryptoexchanges.data.remote.ExchangeRemoteDataSource
import com.cryptoexchanges.data.remote.dto.ExchangeInfoDto
import com.cryptoexchanges.data.remote.dto.ExchangeMapDto
import com.cryptoexchanges.data.remote.dto.ExchangeMarketPairsDto
import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.DomainResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ExchangeRepositoryImplTest {

    private val remoteDataSource = mockk<ExchangeRemoteDataSource>()
    private val repository = ExchangeRepositoryImpl(remoteDataSource)

    @Test
    fun `getExchanges combines map and info into exchanges`() = runTest {
        val map = listOf(ExchangeMapDto(id = 270, name = "Binance"))
        coEvery { remoteDataSource.getExchangeMap() } returns NetworkResult.Success(map)
        coEvery { remoteDataSource.getExchangeInfo(listOf(270)) } returns NetworkResult.Success(
            mapOf("270" to ExchangeInfoDto(id = 270, name = "Binance", logo = "logo.png", spotVolumeUsd = 100.0)),
        )

        val result = repository.getExchanges()

        val expected = DomainResult.Success(
            listOf(
                com.cryptoexchanges.domain.model.Exchange(
                    id = 270,
                    name = "Binance",
                    logoUrl = "logo.png",
                    spotVolumeUsd = 100.0,
                    dateLaunched = null,
                ),
            ),
        )
        assertEquals(expected, result)
    }

    @Test
    fun `getExchanges returns empty list when map is empty`() = runTest {
        coEvery { remoteDataSource.getExchangeMap() } returns NetworkResult.Success(emptyList())

        val result = repository.getExchanges()

        assertEquals(DomainResult.Success(emptyList<com.cryptoexchanges.domain.model.Exchange>()), result)
    }

    @Test
    fun `getExchanges propagates map error`() = runTest {
        coEvery { remoteDataSource.getExchangeMap() } returns NetworkResult.Error(NetworkError.NoConnectivity)

        val result = repository.getExchanges()

        assertEquals(DomainResult.Error(DomainError.NoConnectivity), result)
    }

    @Test
    fun `getExchanges propagates info error`() = runTest {
        coEvery { remoteDataSource.getExchangeMap() } returns NetworkResult.Success(
            listOf(ExchangeMapDto(id = 270, name = "Binance")),
        )
        coEvery { remoteDataSource.getExchangeInfo(listOf(270)) } returns NetworkResult.Error(NetworkError.Http(500, null))

        val result = repository.getExchanges()

        assertEquals(DomainResult.Error(DomainError.ServerError(500)), result)
    }

    @Test
    fun `getExchangeDetail combines info and market pairs`() = runTest {
        val info = ExchangeInfoDto(id = 270, name = "Binance", logo = "logo.png")
        coEvery { remoteDataSource.getExchangeInfo(listOf(270)) } returns NetworkResult.Success(mapOf("270" to info))
        coEvery { remoteDataSource.getExchangeMarketPairs(270) } returns NetworkResult.Success(
            ExchangeMarketPairsDto(id = 270, name = "Binance", marketPairs = emptyList()),
        )

        val result = repository.getExchangeDetail(270)

        assertEquals(270, (result as DomainResult.Success).data.id)
    }

    @Test
    fun `getExchangeDetail returns NotFound when id is missing from info response`() = runTest {
        coEvery { remoteDataSource.getExchangeInfo(listOf(999)) } returns NetworkResult.Success(emptyMap())
        coEvery { remoteDataSource.getExchangeMarketPairs(999) } returns NetworkResult.Error(NetworkError.NoConnectivity)

        val result = repository.getExchangeDetail(999)

        assertEquals(DomainResult.Error(DomainError.NotFound), result)
    }

    @Test
    fun `getExchangeDetail degrades to empty currencies when market pairs call fails`() = runTest {
        val info = ExchangeInfoDto(id = 270, name = "Binance")
        coEvery { remoteDataSource.getExchangeInfo(listOf(270)) } returns NetworkResult.Success(mapOf("270" to info))
        coEvery { remoteDataSource.getExchangeMarketPairs(270) } returns NetworkResult.Error(NetworkError.Timeout)

        val result = repository.getExchangeDetail(270)

        assertEquals(emptyList<com.cryptoexchanges.domain.model.Currency>(), (result as DomainResult.Success).data.currencies)
    }
}
