package com.cryptoexchanges.core.ds.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cryptoexchanges.core.ds.preview.CryptoPreviewWrapper
import com.cryptoexchanges.core.ds.preview.CryptoThemePreviews
import com.cryptoexchanges.core.ds.theme.CryptoDimens

/** Section heading, e.g. above a group of fee rows in the exchange detail screen. */
@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

/** Label/value pair on a single row, e.g. `maker_fee` on the left and `0.10%` on the right. */
@Composable
fun LabelValueRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@CryptoThemePreviews
@Composable
private fun CryptoTextPreview() {
    CryptoPreviewWrapper {
        Column(verticalArrangement = Arrangement.spacedBy(CryptoDimens.spacingS)) {
            SectionTitle(text = "Taxas")
            LabelValueRow(label = "maker_fee", value = "0.10%")
            LabelValueRow(label = "taker_fee", value = "0.20%")
            LabelValueRow(label = "id", value = "binance")
        }
    }
}
