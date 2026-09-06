package com.tihasg.crypto.exchanges.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cryptoexchanges.core.ds.components.CryptoCard
import com.cryptoexchanges.core.ds.components.CryptoLogo
import com.cryptoexchanges.core.ds.components.CryptoSearchField
import com.cryptoexchanges.core.ds.components.CryptoTopBar
import com.cryptoexchanges.core.ds.components.EmptyView
import com.cryptoexchanges.core.ds.components.ErrorView
import com.cryptoexchanges.core.ds.components.LoadingView
import com.cryptoexchanges.core.ds.theme.CryptoDimens
import com.cryptoexchanges.domain.model.Exchange
import com.tihasg.crypto.exchanges.R
import com.tihasg.crypto.exchanges.presentation.common.formatDate
import com.tihasg.crypto.exchanges.presentation.common.formatUsd
import com.tihasg.crypto.exchanges.presentation.common.toMessage
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExchangeListScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExchangeListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ExchangeListEffect.NavigateToDetail -> onNavigateToDetail(effect.exchangeId)
            }
        }
    }

    ExchangeListContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onAboutClick = onNavigateToAbout,
        modifier = modifier,
    )
}

@Composable
internal fun ExchangeListContent(
    uiState: ExchangeListUiState,
    onIntent: (ExchangeListIntent) -> Unit,
    onAboutClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CryptoTopBar(
                title = stringResource(R.string.exchange_list_title),
                actions = {
                    IconButton(onClick = onAboutClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.exchange_list_about_content_description),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            when {
                uiState.isLoading -> LoadingView(modifier = Modifier.weight(1f))
                uiState.error != null && uiState.exchanges.isEmpty() -> ErrorView(
                    message = uiState.error.toMessage(),
                    onRetry = { onIntent(ExchangeListIntent.OnRetry) },
                    modifier = Modifier.weight(1f),
                )

                else -> {
                    CryptoSearchField(
                        query = uiState.searchQuery,
                        onQueryChange = { onIntent(ExchangeListIntent.OnSearchQueryChange(it)) },
                        placeholder = stringResource(R.string.exchange_list_search_placeholder),
                        clearContentDescription = stringResource(R.string.exchange_list_search_clear_content_description),
                        modifier = Modifier.padding(CryptoDimens.spacingM),
                    )
                    when {
                        uiState.exchanges.isEmpty() && uiState.searchQuery.isNotBlank() -> EmptyView(
                            message = stringResource(
                                R.string.exchange_list_search_empty,
                                uiState.searchQuery
                            ),
                            modifier = Modifier.weight(1f),
                        )

                        uiState.exchanges.isEmpty() -> EmptyView(
                            message = stringResource(R.string.exchange_list_empty),
                            modifier = Modifier.weight(1f),
                        )

                        else -> ExchangeList(
                            exchanges = uiState.exchanges,
                            onExchangeClick = { onIntent(ExchangeListIntent.OnExchangeClick(it)) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExchangeList(
    exchanges: List<Exchange>,
    onExchangeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(CryptoDimens.spacingM),
        verticalArrangement = Arrangement.spacedBy(CryptoDimens.spacingS),
    ) {
        items(exchanges, key = { it.id }) { exchange ->
            ExchangeListItem(exchange = exchange, onClick = { onExchangeClick(exchange.id) })
        }
    }
}

@Composable
private fun ExchangeListItem(
    exchange: Exchange,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CryptoCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CryptoLogo(
                url = exchange.logoUrl,
                contentDescription = exchange.name,
                size = CryptoDimens.logoM,
            )
            Spacer(modifier = Modifier.width(CryptoDimens.spacingM))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = exchange.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = formatUsd(exchange.spotVolumeUsd),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = formatDate(exchange.dateLaunched),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
