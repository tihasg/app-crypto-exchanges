package com.cryptoexchanges.core.ds.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cryptoexchanges.core.ds.preview.CryptoPreviewWrapper
import com.cryptoexchanges.core.ds.preview.CryptoThemePreviews
import com.cryptoexchanges.core.ds.theme.CryptoDimens

@Composable
fun CryptoCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = MaterialTheme.shapes.medium
    val colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    )
    val elevation = CardDefaults.cardElevation(defaultElevation = CryptoDimens.elevationS)

    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier, shape = shape, colors = colors, elevation = elevation) {
            Column(modifier = Modifier.padding(CryptoDimens.spacingM), content = content)
        }
    } else {
        Card(modifier = modifier, shape = shape, colors = colors, elevation = elevation) {
            Column(modifier = Modifier.padding(CryptoDimens.spacingM), content = content)
        }
    }
}

@CryptoThemePreviews
@Composable
private fun CryptoCardPreview() {
    CryptoPreviewWrapper {
        CryptoCard(onClick = {}) {
            Text(text = "Binance", style = MaterialTheme.typography.titleMedium)
            LabelValueRow(label = "maker_fee", value = "0.10%")
            LabelValueRow(label = "taker_fee", value = "0.20%")
        }
    }
}
