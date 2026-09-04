package com.cryptoexchanges.core.ds.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Applies the CryptoExchanges Material 3 theme (colors, typography, shapes) and exposes the
 * semantic [CryptoColors] tokens via [LocalCryptoColors]. Wrap the app/screen root with this —
 * every [com.cryptoexchanges.core.ds.components] composable expects to be hosted inside it.
 */
@Composable
fun CryptoExchangesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) CryptoDarkColorScheme else CryptoLightColorScheme
    val cryptoColors = if (darkTheme) DarkCryptoColors else LightCryptoColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalCryptoColors provides cryptoColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = CryptoTypography,
            shapes = CryptoShapes,
            content = content,
        )
    }
}
