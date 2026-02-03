package com.example.screen_navigation.ui.navargs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
    
/**
 * Sealed class for type-safe navigation routes.
 * Using sealed class ensures compile-time safety and prevents typos in route strings.
 */
sealed class NavArgsRoute(val route: String) {
    
    // Static route - no arguments needed
    data object Home : NavArgsRoute(route = "home")
    
    // Dynamic route with arguments
    data object Profile : NavArgsRoute(route = "profile/{name}/{score}") {
        // Argument keys as constants
        const val ARG_NAME = "name"
        const val ARG_SCORE = "score"
        
        // Helper function to build route with actual values
        fun createRoute(name: String, score: String): String {
            return "profile/$name/$score"
        }
    }
}

@Composable
fun AppNavArgs(modifier: Modifier = Modifier) {
    // Step 1: Create NavController
    val navController = rememberNavController()

    // Step 2: Setup NavHost with type-safe startDestination
    NavHost(
        navController = navController,
        startDestination = NavArgsRoute.Home.route
    ) {
        // Step 3: Declare screens using type-safe routes
        
        // Home Screen - static route
        composable(route = NavArgsRoute.Home.route) {
            HomeScreen(
                onNavigate = { name, score ->
                    // Navigate using the helper function for type-safe argument passing
                    navController.navigate(NavArgsRoute.Profile.createRoute(name, score))
                }
            )
        }

        // Profile Screen - dynamic route with arguments
        composable(
            route = NavArgsRoute.Profile.route,
            arguments = listOf(
                navArgument(NavArgsRoute.Profile.ARG_NAME) { type = NavType.StringType },
                navArgument(NavArgsRoute.Profile.ARG_SCORE) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ProfileScreen(
                name = backStackEntry.arguments?.getString(NavArgsRoute.Profile.ARG_NAME),
                score = backStackEntry.arguments?.getString(NavArgsRoute.Profile.ARG_SCORE)
            )
        }
    }
}