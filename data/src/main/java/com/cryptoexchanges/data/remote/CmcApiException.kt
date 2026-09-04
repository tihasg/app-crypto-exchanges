package com.cryptoexchanges.data.remote

class CmcApiException(val errorCode: Int, message: String?) : Exception(message)
