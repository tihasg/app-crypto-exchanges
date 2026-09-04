package com.cryptoexchanges.core.ds.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cryptoexchanges.core.ds.preview.CryptoPreviewWrapper
import com.cryptoexchanges.core.ds.preview.CryptoThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        modifier = modifier,
        navigationIcon = {
            onBackClick?.let { onClick ->
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@CryptoThemePreviews
@Composable
private fun CryptoTopBarRootPreview() {
    CryptoPreviewWrapper {
        CryptoTopBar(title = "Exchanges")
    }
}

@CryptoThemePreviews
@Composable
private fun CryptoTopBarDetailPreview() {
    CryptoPreviewWrapper {
        CryptoTopBar(title = "Binance", onBackClick = {})
    }
}
