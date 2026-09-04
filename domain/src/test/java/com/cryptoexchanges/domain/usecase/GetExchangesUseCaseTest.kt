package com.cryptoexchanges.domain.usecase

import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.repository.ExchangeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetExchangesUseCaseTest {

    private val repository = mockk<ExchangeRepository>()
    private val useCase = GetExchangesUseCase(repository)

    @Test
    fun `sorts exchanges by spot volume descending`() = runTest {
        val binance = Exchange(id = 1, name = "Binance", logoUrl = null, spotVolumeUsd = 100.0, dateLaunched = null)
        val coinbase = Exchange(id = 2, name = "Coinbase", logoUrl = null, spotVolumeUsd = 500.0, dateLaunched = null)
        val kraken = Exchange(id = 3, name = "Kraken", logoUrl = null, spotVolumeUsd = 200.0, dateLaunched = null)
        coEvery { repository.getExchanges() } returns DomainResult.Success(listOf(binance, coinbase, kraken))

        val result = useCase()

        assertEquals(DomainResult.Success(listOf(coinbase, kraken, binance)), result)
    }

    @Test
    fun `sorts exchanges with unknown volume last`() = runTest {
        val known = Exchange(id = 1, name = "Binance", logoUrl = null, spotVolumeUsd = 100.0, dateLaunched = null)
        val unknown = Exchange(id = 2, name = "Unknown", logoUrl = null, spotVolumeUsd = null, dateLaunched = null)
        coEvery { repository.getExchanges() } returns DomainResult.Success(listOf(unknown, known))

        val result = useCase()

        assertEquals(DomainResult.Success(listOf(known, unknown)), result)
    }

    @Test
    fun `propagates repository error without sorting`() = runTest {
        coEvery { repository.getExchanges() } returns DomainResult.Error(DomainError.NoConnectivity)

        val result = useCase()

        assertEquals(DomainResult.Error(DomainError.NoConnectivity), result)
    }
}
