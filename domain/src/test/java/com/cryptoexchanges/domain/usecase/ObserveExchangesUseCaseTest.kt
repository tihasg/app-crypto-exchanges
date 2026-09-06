package com.cryptoexchanges.domain.usecase

import com.cryptoexchanges.domain.model.Exchange
import com.cryptoexchanges.domain.repository.ExchangeRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveExchangesUseCaseTest {

    private val repository = mockk<ExchangeRepository>()
    private val useCase = ObserveExchangesUseCase(repository)

    @Test
    fun `emits whatever the repository observes from cache`() = runTest {
        val exchanges = listOf(
            Exchange(
                id = 1,
                name = "Binance",
                logoUrl = null,
                spotVolumeUsd = 100.0,
                dateLaunched = null
            )
        )
        every { repository.observeCachedExchanges() } returns flowOf(exchanges)

        val result = useCase().first()

        assertEquals(exchanges, result)
    }
}
