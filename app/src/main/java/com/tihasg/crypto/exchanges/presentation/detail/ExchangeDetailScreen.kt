package com.tihasg.crypto.exchanges.presentation.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cryptoexchanges.core.ds.components.CryptoLogo
import com.cryptoexchanges.core.ds.components.CryptoTopBar
import com.cryptoexchanges.core.ds.components.EmptyView
import com.cryptoexchanges.core.ds.components.ErrorView
import com.cryptoexchanges.core.ds.components.LabelValueRow
import com.cryptoexchanges.core.ds.components.LoadingView
import com.cryptoexchanges.core.ds.components.SectionTitle
import com.cryptoexchanges.core.ds.theme.CryptoDimens
import com.cryptoexchanges.domain.model.ExchangeDetail
import com.tihasg.crypto.exchanges.presentation.common.formatDate
import com.tihasg.crypto.exchanges.presentation.common.formatPercent
import com.tihasg.crypto.exchanges.presentation.common.formatUsd
import com.tihasg.crypto.exchanges.presentation.common.toMessage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ExchangeDetailScreen(
    exchangeId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExchangeDetailViewModel = koinViewModel { parametersOf(exchangeId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ExchangeDetailEffect.OpenUrl -> uriHandler.openUri(effect.url)
                ExchangeDetailEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    ExchangeDetailContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
internal fun ExchangeDetailContent(
    uiState: ExchangeDetailUiState,
    onIntent: (ExchangeDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CryptoTopBar(
                title = uiState.exchangeDetail?.name ?: "Detalhes",
                onBackClick = { onIntent(ExchangeDetailIntent.OnBackClick) },
                backContentDescription = "Voltar",
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            when {
                uiState.isLoading -> LoadingView()
                uiState.error != null -> ErrorView(
                    message = uiState.error.toMessage(),
                    onRetry = { onIntent(ExchangeDetailIntent.OnRetry) },
                )
                uiState.exchangeDetail != null -> ExchangeDetailBody(
                    detail = uiState.exchangeDetail,
                    onWebsiteClick = { onIntent(ExchangeDetailIntent.OnWebsiteClick) },
                )
                else -> EmptyView(message = "Nenhum detalhe disponível")
            }
        }
    }
}

@Composable
private fun ExchangeDetailBody(
    detail: ExchangeDetail,
    onWebsiteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(CryptoDimens.spacingM),
        verticalArrangement = Arrangement.spacedBy(CryptoDimens.spacingM),
    ) {
        item {
            ExchangeDetailHeader(detail = detail, onWebsiteClick = onWebsiteClick)
        }
        item {
            SectionTitle(text = "Moedas negociadas")
        }
        if (detail.currencies.isEmpty()) {
            item {
                Text(
                    text = "Nenhuma moeda disponível para esta exchange.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            items(detail.currencies, key = { it.name }) { currency ->
                LabelValueRow(label = currency.name, value = formatUsd(currency.priceUsd))
            }
        }
    }
}

@Composable
private fun ExchangeDetailHeader(
    detail: ExchangeDetail,
    onWebsiteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CryptoLogo(url = detail.logoUrl, contentDescription = detail.name, size = CryptoDimens.logoL)
            Spacer(modifier = Modifier.width(CryptoDimens.spacingM))
            Column {
                Text(text = detail.name, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "ID: ${detail.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        val description = detail.description
        if (!description.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(CryptoDimens.spacingM))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }

        val websiteUrl = detail.websiteUrl
        if (!websiteUrl.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(CryptoDimens.spacingS))
            Text(
                text = websiteUrl,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(onClick = onWebsiteClick),
            )
        }

        Spacer(modifier = Modifier.height(CryptoDimens.spacingM))
        LabelValueRow(label = "maker_fee", value = formatPercent(detail.makerFee))
        LabelValueRow(label = "taker_fee", value = formatPercent(detail.takerFee))
        LabelValueRow(label = "date_launched", value = formatDate(detail.dateLaunched))
    }
}
