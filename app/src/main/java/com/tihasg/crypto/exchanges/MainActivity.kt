package com.tihasg.crypto.exchanges

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cryptoexchanges.core.ds.theme.CryptoExchangesTheme
import com.tihasg.crypto.exchanges.navigation.CryptoExchangesNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoExchangesTheme {
                CryptoExchangesNavHost()
            }
        }
    }
}
