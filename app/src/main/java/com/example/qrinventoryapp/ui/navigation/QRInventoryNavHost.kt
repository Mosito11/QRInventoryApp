package com.example.qrinventoryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.qrinventoryapp.ui.home.HomeDestination
import com.example.qrinventoryapp.ui.home.HomeScreen
import com.example.qrinventoryapp.ui.scan.ScanScreen
import com.example.qrinventoryapp.ui.scan.ScanScreenDestination

@Composable
fun QRInventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                /*navigateToScan = { TODO },
                navigateToQuit = { TODO }
                */
            )
        }
        composable(route = ScanScreenDestination.route) {
            ScanScreen(
                /*navigateBack = { TODO },
                saveIncorrectItem = { TODO },
                newScan = { TODO } */
            )
        }
    }

}