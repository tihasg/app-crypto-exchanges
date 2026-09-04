package com.cryptoexchanges.data.repository

import com.cryptoexchanges.domain.model.DomainError
import com.cryptoexchanges.domain.model.DomainResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeExchangeRepositoryTest {

    private val repository = FakeExchangeRepository()

    @Test
    fun `getExchanges returns a non-empty canned list`() = runTest {
        val result = repository.getExchanges()

        assertTrue(result is DomainResult.Success)
        assertTrue((result as DomainResult.Success).data.isNotEmpty())
    }

    @Test
    fun `getExchangeDetail returns detail for a known id`() = runTest {
        val result = repository.getExchangeDetail(270)

        assertTrue(result is DomainResult.Success)
        assertEquals("Binance", (result as DomainResult.Success).data.name)
    }

    @Test
    fun `getExchangeDetail returns NotFound for an unknown id`() = runTest {
        val result = repository.getExchangeDetail(-1)

        assertEquals(DomainResult.Error(DomainError.NotFound), result)
    }
}
