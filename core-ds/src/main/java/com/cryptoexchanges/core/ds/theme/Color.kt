package com.cryptoexchanges.core.ds.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val Red500 = Color(0xFFE53935)
private val Red700 = Color(0xFFB71C1C)
private val Red100 = Color(0xFFFFCDD2)

private val White = Color(0xFFFFFFFF)
private val OffWhite = Color(0xFFF7F7F7)

private val Neutral900 = Color(0xFF1A1A1A)
private val Neutral700 = Color(0xFF3D3D3D)
private val Neutral500 = Color(0xFF757575)
private val Neutral200 = Color(0xFFE0E0E0)
private val Neutral100 = Color(0xFFF0F0F0)

private val Success = Color(0xFF2E7D32)
private val Warning = Color(0xFFF9A825)

val CryptoLightColorScheme: ColorScheme = lightColorScheme(
    primary = Red500,
    onPrimary = White,
    primaryContainer = Red100,
    onPrimaryContainer = Red700,
    secondary = Neutral700,
    onSecondary = White,
    background = OffWhite,
    onBackground = Neutral900,
    surface = White,
    onSurface = Neutral900,
    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral500,
    outline = Neutral200,
    error = Red700,
    onError = White,
)

val CryptoDarkColorScheme: ColorScheme = darkColorScheme(
    primary = Red500,
    onPrimary = White,
    primaryContainer = Red700,
    onPrimaryContainer = Red100,
    secondary = Neutral200,
    onSecondary = Neutral900,
    background = Neutral900,
    onBackground = White,
    surface = Neutral700,
    onSurface = White,
    surfaceVariant = Neutral700,
    onSurfaceVariant = Neutral200,
    outline = Neutral500,
    error = Red100,
    onError = Neutral900,
)

data class CryptoColors(
    val positiveValue: Color,
    val negativeValue: Color,
    val warning: Color,
)

val LightCryptoColors = CryptoColors(
    positiveValue = Success,
    negativeValue = Red700,
    warning = Warning,
)

val DarkCryptoColors = CryptoColors(
    positiveValue = Success,
    negativeValue = Red100,
    warning = Warning,
)

val LocalCryptoColors = staticCompositionLocalOf { LightCryptoColors }

val MaterialTheme.cryptoColors: CryptoColors
    @Composable
    get() = LocalCryptoColors.current
