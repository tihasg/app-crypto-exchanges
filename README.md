# app-crypto-exchanges

Aplicativo Android que consulta a API da [CoinMarketCap](https://coinmarketcap.com/api/documentation/v1/)
e exibe uma lista de exchanges de criptomoedas e, ao tocar em uma delas, seus detalhes junto das
moedas negociadas. Construído com Kotlin, Jetpack Compose e arquitetura MVI + Clean Architecture.

> Este README é a versão inicial (escopo da fase de scaffold). Será substituído por uma versão
> completa — com diagrama de arquitetura, decisões e trade-offs — ao final do desenvolvimento.

## Setup

1. Copie `local.properties.example` para `local.properties` (arquivo ignorado pelo git).
2. Preencha `sdk.dir` com o caminho do seu Android SDK.
3. Preencha `CMC_API_KEY` com uma chave da [CoinMarketCap Pro API](https://pro.coinmarketcap.com/account).
   A chave nunca é versionada: ela é lida de `local.properties` e exposta ao código via
   `BuildConfig.CMC_API_KEY` (wiring feito na fase de DI/app).

## Módulos

| Módulo          | Tipo                | Responsabilidade                                                        |
|-----------------|---------------------|---------------------------------------------------------------------------|
| `core-ds`       | Android library      | Design system: cores, tipografia, dimens e componentes Compose reutilizáveis (`Crypto*`). Sem dependência de domínio/dados. |
| `core-network`  | Kotlin/JVM library   | Infra de rede reutilizável: factories de `OkHttpClient`/`Retrofit` (kotlinx.serialization), interceptor de API key e `safeApiCall`/`NetworkResult` para nunca deixar exceção de rede vazar. Sem dependência de Android/Hilt/domínio. |
| `app`           | Android application  | Entry point. Vai concentrar as camadas `domain`/`data`/`presentation` (como módulos ou packages — decisão registrada aqui conforme evolui) e a injeção de dependência (Hilt). |

## Rodando

```
./gradlew build      # compila todos os módulos e roda os testes unitários
./gradlew test        # apenas testes unitários
```

## Testes

- `core-network` já tem testes unitários (JUnit4 + MockWebServer + kotlinx-coroutines-test)
  cobrindo o interceptor de API key e o mapeamento de exceções de rede em `NetworkResult`.
- Demais camadas (domain/data/presentation) terão testes unitários e de UI adicionados
  conforme cada fase for implementada.
