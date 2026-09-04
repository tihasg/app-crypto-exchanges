package com.cryptoexchanges.core.ds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.SubcomposeAsyncImage
import com.cryptoexchanges.core.ds.preview.CryptoPreviewWrapper
import com.cryptoexchanges.core.ds.preview.CryptoThemePreviews
import com.cryptoexchanges.core.ds.theme.CryptoDimens

@Composable
fun CryptoLogo(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = CryptoDimens.logoM,
    shape: Shape = CircleShape,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.size(size / 2),
                    strokeWidth = CryptoDimens.borderWidthS,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            error = {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(size / 2),
                )
            },
        )
    }
}

@CryptoThemePreviews
@Composable
private fun CryptoLogoPreview() {
    CryptoPreviewWrapper {
        CryptoLogo(url = null, contentDescription = "Binance", size = CryptoDimens.logoL)
    }
}
