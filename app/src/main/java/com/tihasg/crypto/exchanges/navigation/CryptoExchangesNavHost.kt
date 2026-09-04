package com.tihasg.crypto.exchanges.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

@Composable
fun CryptoExchangesNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ExchangeListRoute) {
        composable<ExchangeListRoute> {
            Text("Exchange list - coming soon")
        }
        composable<ExchangeDetailRoute> { backStackEntry ->
            val route: ExchangeDetailRoute = backStackEntry.toRoute()
            Text("Exchange detail ${route.exchangeId} - coming soon")
        }
    }
}
