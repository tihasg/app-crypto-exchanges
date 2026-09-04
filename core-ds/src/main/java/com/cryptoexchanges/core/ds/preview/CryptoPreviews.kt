package com.cryptoexchanges.core.ds.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cryptoexchanges.core.ds.theme.CryptoDimens
import com.cryptoexchanges.core.ds.theme.CryptoExchangesTheme

/**
 * Multipreview annotation rendering a component in both light and dark mode. Apply to every
 * component `@Preview` function in this module instead of a bare `@Preview`.
 */
@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class CryptoThemePreviews

/** Wraps a preview with [CryptoExchangesTheme] plus a themed background and padding. */
@Composable
fun CryptoPreviewWrapper(content: @Composable () -> Unit) {
    CryptoExchangesTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.padding(CryptoDimens.spacingM)) {
                content()
            }
        }
    }
}
