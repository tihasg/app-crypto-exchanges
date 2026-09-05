package com.tihasg.crypto.exchanges.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tihasg.crypto.exchanges.presentation.about.AboutScreen
import com.tihasg.crypto.exchanges.presentation.detail.ExchangeDetailScreen
import com.tihasg.crypto.exchanges.presentation.list.ExchangeListScreen

@Composable
fun CryptoExchangesNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ExchangeListRoute) {
        composable<ExchangeListRoute> {
            ExchangeListScreen(
                onNavigateToDetail = { exchangeId ->
                    navController.navigate(ExchangeDetailRoute(exchangeId))
                },
                onNavigateToAbout = { navController.navigate(AboutRoute) },
            )
        }
        composable<ExchangeDetailRoute> { backStackEntry ->
            val route: ExchangeDetailRoute = backStackEntry.toRoute()
            ExchangeDetailScreen(
                exchangeId = route.exchangeId,
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<AboutRoute> {
            AboutScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
