package com.cryptoexchanges.core.network

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall())
    } catch (e: SocketTimeoutException) {
        NetworkResult.Error(NetworkError.Timeout)
    } catch (e: UnknownHostException) {
        NetworkResult.Error(NetworkError.NoConnectivity)
    } catch (e: IOException) {
        NetworkResult.Error(NetworkError.NoConnectivity)
    } catch (e: HttpException) {
        NetworkResult.Error(NetworkError.Http(code = e.code(), message = e.message()))
    } catch (e: SerializationException) {
        NetworkResult.Error(NetworkError.Serialization)
    } catch (e: Exception) {
        NetworkResult.Error(NetworkError.Unknown(e.message))
    }
}
