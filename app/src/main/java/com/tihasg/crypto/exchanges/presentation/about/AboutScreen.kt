package com.tihasg.crypto.exchanges.presentation.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import com.cryptoexchanges.core.ds.components.CryptoTopBar
import com.cryptoexchanges.core.ds.components.SectionTitle
import com.cryptoexchanges.core.ds.theme.CryptoDimens
import com.tihasg.crypto.exchanges.R

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        modifier = modifier,
        topBar = {
            CryptoTopBar(
                title = stringResource(R.string.about_title),
                onBackClick = onNavigateBack,
                backContentDescription = stringResource(R.string.about_back_content_description),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(CryptoDimens.spacingM),
            verticalArrangement = Arrangement.spacedBy(CryptoDimens.spacingL),
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(R.string.about_name), style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = stringResource(R.string.about_role),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(R.string.about_location),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(CryptoDimens.spacingXs),
                ) {
                    SectionTitle(text = stringResource(R.string.about_contact_section_title))
                    Spacer(modifier = Modifier.height(CryptoDimens.spacingXs))
                    AboutLink(
                        text = stringResource(R.string.about_phone),
                        onClick = { uriHandler.openUri("tel:+5511945418403") },
                    )
                    AboutLink(
                        text = stringResource(R.string.about_email),
                        onClick = { uriHandler.openUri("mailto:tiagolimarodrigues@hotmail.com") },
                    )
                    AboutLink(
                        text = stringResource(R.string.about_linkedin),
                        onClick = { uriHandler.openUri("https://linkedin.com/in/tihasg") },
                    )
                    AboutLink(
                        text = stringResource(R.string.about_github),
                        onClick = { uriHandler.openUri("https://github.com/tihasg") },
                    )
                }
            }
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionTitle(text = stringResource(R.string.about_summary_title))
                    Spacer(modifier = Modifier.height(CryptoDimens.spacingXs))
                    Text(
                        text = stringResource(R.string.about_summary_body),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline,
        modifier = modifier.clickable(onClick = onClick),
    )
}