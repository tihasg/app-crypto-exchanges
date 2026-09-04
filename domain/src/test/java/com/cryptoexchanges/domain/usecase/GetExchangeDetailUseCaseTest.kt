package com.cryptoexchanges.domain.usecase

import com.cryptoexchanges.domain.model.Currency
import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.model.ExchangeDetail
import com.cryptoexchanges.domain.repository.ExchangeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetExchangeDetailUseCaseTest {

    private val repository = mockk<ExchangeRepository>()
    private val useCase = GetExchangeDetailUseCase(repository)

    @Test
    fun `returns detail from repository for given id`() = runTest {
        val detail = ExchangeDetail(
            id = 1,
            name = "Binance",
            logoUrl = null,
            description = "Largest crypto exchange",
            websiteUrl = "https://binance.com",
            makerFee = 0.001,
            takerFee = 0.001,
            dateLaunched = "2017-05-25T00:00:00.000Z",
            currencies = listOf(Currency(name = "Bitcoin", priceUsd = 60000.0)),
        )
        coEvery { repository.getExchangeDetail(1) } returns DomainResult.Success(detail)

        val result = useCase(1)

        assertEquals(DomainResult.Success(detail), result)
        coVerify(exactly = 1) { repository.getExchangeDetail(1) }
    }

    @Test
    fun `propagates repository error`() = runTest {
        coEvery { repository.getExchangeDetail(99) } returns DomainResult.Error(DomainError.NotFound)

        val result = useCase(99)

        assertEquals(DomainResult.Error(DomainError.NotFound), result)
    }
}
