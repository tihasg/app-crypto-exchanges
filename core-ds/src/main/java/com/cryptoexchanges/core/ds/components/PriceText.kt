package com.cryptoexchanges.core.ds.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.cryptoexchanges.core.ds.preview.CryptoPreviewWrapper
import com.cryptoexchanges.core.ds.preview.CryptoThemePreviews
import com.cryptoexchanges.core.ds.theme.CryptoDimens
import com.cryptoexchanges.core.ds.theme.cryptoColors

@Composable
fun PriceText(
    value: String,
    isPositive: Boolean?,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val color = when (isPositive) {
        true -> MaterialTheme.cryptoColors.positiveValue
        false -> MaterialTheme.cryptoColors.negativeValue
        null -> MaterialTheme.colorScheme.onSurface
    }
    Text(text = value, modifier = modifier, style = style, color = color)
}

@CryptoThemePreviews
@Composable
private fun PriceTextPreview() {
    CryptoPreviewWrapper {
        Column(verticalArrangement = Arrangement.spacedBy(CryptoDimens.spacingXs)) {
            PriceText(value = "+2.35%", isPositive = true)
            PriceText(value = "-1.20%", isPositive = false)
            PriceText(value = "0.00%", isPositive = null)
        }
    }
}
