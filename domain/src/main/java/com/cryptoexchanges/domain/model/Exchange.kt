package com.cryptoexchanges.domain.model

data class Exchange(
    val id: Int,
    val name: String,
    val logoUrl: String?,
    val spotVolumeUsd: Double?,
    val dateLaunched: String?,
)

fun List<Exchange>.sortedByVolumeDescending(): List<Exchange> =
    sortedByDescending { it.spotVolumeUsd ?: Double.NEGATIVE_INFINITY }
