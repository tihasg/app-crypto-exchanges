package com.cryptoexchanges.core.ds.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.cryptoexchanges.core.ds.preview.CryptoPreviewWrapper
import com.cryptoexchanges.core.ds.preview.CryptoThemePreviews
import com.cryptoexchanges.core.ds.theme.CryptoDimens

/** Centered spinner for a screen/section that is loading. */
@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

/** Icon + message for a screen/section that loaded successfully but has no data. */
@Composable
fun EmptyView(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Info,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(CryptoDimens.spacingL),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(CryptoDimens.iconL),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(CryptoDimens.spacingM))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Icon + message + retry action for a screen/section that failed to load. Key composable for
 * the app's resiliency requirement: every failure surface should route through this.
 */
@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(CryptoDimens.spacingL),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            modifier = Modifier.size(CryptoDimens.iconL),
            tint = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(CryptoDimens.spacingM))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(CryptoDimens.spacingM))
        CryptoButton(text = "Tentar novamente", onClick = onRetry)
    }
}

@CryptoThemePreviews
@Composable
private fun LoadingViewPreview() {
    CryptoPreviewWrapper {
        LoadingView(modifier = Modifier.size(CryptoDimens.logoL * 3))
    }
}

@CryptoThemePreviews
@Composable
private fun EmptyViewPreview() {
    CryptoPreviewWrapper {
        EmptyView(
            message = "Nenhuma exchange encontrada",
            modifier = Modifier.size(CryptoDimens.logoL * 3),
        )
    }
}

@CryptoThemePreviews
@Composable
private fun ErrorViewPreview() {
    CryptoPreviewWrapper {
        ErrorView(
            message = "Não foi possível carregar as exchanges",
            onRetry = {},
            modifier = Modifier.size(CryptoDimens.logoL * 3),
        )
    }
}
