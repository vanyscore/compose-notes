package com.vanyscore.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanyscore.app.MainScreen

@Composable
fun App() {
    return NavHost(
        navController = rememberNavController(),
        startDestination = AppRoutes.Main
    ) {
        composable(AppRoutes.Main) {
            MainScreen()
        }
    }
}