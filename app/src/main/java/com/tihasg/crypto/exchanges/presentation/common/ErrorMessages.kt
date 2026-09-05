package com.tihasg.crypto.exchanges.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cryptoexchanges.domain.model.DomainError
import com.tihasg.crypto.exchanges.R

@Composable
fun DomainError.toMessage(): String = when (this) {
    DomainError.NoConnectivity -> stringResource(R.string.error_no_connectivity)
    DomainError.Timeout -> stringResource(R.string.error_timeout)
    DomainError.Unauthorized -> stringResource(R.string.error_unauthorized)
    DomainError.NotFound -> stringResource(R.string.error_not_found)
    is DomainError.ServerError -> stringResource(R.string.error_server_format, code)
    DomainError.Parsing -> stringResource(R.string.error_parsing)
    is DomainError.Unknown -> message ?: stringResource(R.string.error_unknown_fallback)
}
