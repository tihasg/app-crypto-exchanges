package com.tihasg.crypto.exchanges.presentation.common

import com.cryptoexchanges.domain.model.DomainError

fun DomainError.toMessage(): String = when (this) {
    DomainError.NoConnectivity -> "Sem conexão com a internet. Verifique sua rede e tente novamente."
    DomainError.Timeout -> "A requisição demorou demais. Tente novamente."
    DomainError.Unauthorized -> "Chave de API inválida ou sem permissão para este recurso."
    DomainError.NotFound -> "Exchange não encontrada."
    is DomainError.ServerError -> "Erro no servidor (código $code). Tente novamente mais tarde."
    DomainError.Parsing -> "Não foi possível processar a resposta do servidor."
    is DomainError.Unknown -> message ?: "Ocorreu um erro inesperado."
}
