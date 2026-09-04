package com.cryptoexchanges.data.mapper

import com.cryptoexchanges.core.network.NetworkError
import com.cryptoexchanges.domain.model.DomainError
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkErrorMapperTest {

    @Test
    fun `maps NoConnectivity`() {
        assertEquals(DomainError.NoConnectivity, NetworkError.NoConnectivity.toDomainError())
    }

    @Test
    fun `maps Timeout`() {
        assertEquals(DomainError.Timeout, NetworkError.Timeout.toDomainError())
    }

    @Test
    fun `maps Serialization to Parsing`() {
        assertEquals(DomainError.Parsing, NetworkError.Serialization.toDomainError())
    }

    @Test
    fun `maps Http 401 and 403 to Unauthorized`() {
        assertEquals(DomainError.Unauthorized, NetworkError.Http(401, null).toDomainError())
        assertEquals(DomainError.Unauthorized, NetworkError.Http(403, null).toDomainError())
    }

    @Test
    fun `maps Http 404 to NotFound`() {
        assertEquals(DomainError.NotFound, NetworkError.Http(404, null).toDomainError())
    }

    @Test
    fun `maps other Http codes to ServerError with the code`() {
        assertEquals(DomainError.ServerError(500), NetworkError.Http(500, "boom").toDomainError())
        assertEquals(DomainError.ServerError(429), NetworkError.Http(429, null).toDomainError())
    }

    @Test
    fun `maps Unknown preserving the message`() {
        assertEquals(DomainError.Unknown("weird"), NetworkError.Unknown("weird").toDomainError())
    }
}
