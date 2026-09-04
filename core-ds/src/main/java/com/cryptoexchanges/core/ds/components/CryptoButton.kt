package com.cryptoexchanges.core.ds.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cryptoexchanges.core.ds.preview.CryptoPreviewWrapper
import com.cryptoexchanges.core.ds.preview.CryptoThemePreviews
import com.cryptoexchanges.core.ds.theme.CryptoDimens

/** Visual style of [CryptoButton]: filled (primary action) or outlined (secondary action). */
enum class CryptoButtonVariant { Primary, Secondary }

/**
 * Primary action button of the design system. While [isLoading] is true it shows a spinner
 * in place of [text] and disables the click, regardless of [enabled].
 */
@Composable
fun CryptoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    variant: CryptoButtonVariant = CryptoButtonVariant.Primary,
) {
    val isClickable = enabled && !isLoading
    when (variant) {
        CryptoButtonVariant.Primary -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = isClickable,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            CryptoButtonContent(
                text = text,
                isLoading = isLoading,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        }

        CryptoButtonVariant.Secondary -> OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = isClickable,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            border = BorderStroke(CryptoDimens.borderWidthS, MaterialTheme.colorScheme.primary),
        ) {
            CryptoButtonContent(
                text = text,
                isLoading = isLoading,
                contentColor = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun CryptoButtonContent(text: String, isLoading: Boolean, contentColor: Color) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(CryptoDimens.iconS),
            color = contentColor,
            strokeWidth = CryptoDimens.progressStrokeWidth,
        )
    } else {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@CryptoThemePreviews
@Composable
private fun CryptoButtonPreview() {
    CryptoPreviewWrapper {
        Column(verticalArrangement = Arrangement.spacedBy(CryptoDimens.spacingS)) {
            CryptoButton(text = "Ver detalhes", onClick = {})
            CryptoButton(text = "Ver detalhes", onClick = {}, variant = CryptoButtonVariant.Secondary)
            CryptoButton(text = "Carregando", onClick = {}, isLoading = true)
            CryptoButton(text = "Desabilitado", onClick = {}, enabled = false)
        }
    }
}
