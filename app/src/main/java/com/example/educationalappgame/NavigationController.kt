package com.example.educationalappgame

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") { MainScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("level_selection") { LevelSelectionScreen(navController) }
        composable("level1_game1") { Game1Level1Screen(navController) }
        composable("level1_game2") { Game2Level1Screen(navController) }
        composable("level1_game3") { Game3Level1Screen(navController) }
        composable("level2_game1") { Game1Level2Screen(navController) }
        //composable("level2_game2") { Game2Level2Screen(navController) }
        composable("level2_game3") { Game3Level2Screen(navController) }
    }
}
