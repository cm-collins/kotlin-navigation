package com.example.screen_navigation.ui.nestednav.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.screen_navigation.ui.nestednav.screens.CheckoutScreen
import com.example.screen_navigation.ui.nestednav.screens.ConfirmationScreen
import com.example.screen_navigation.ui.nestednav.screens.HomeScreen
import com.example.screen_navigation.ui.nestednav.screens.LogoutScreen
import com.example.screen_navigation.ui.nestednav.screens.ProfileScreen

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    // startDestination must be a DESTINATION inside this graph (a composable route),
    // not the graph route itself.
    navigation(
        startDestination = AppRoutes.HomeScreen.route,
        route = AppRoutes.Main.route
    )
{
    //home screens
    composable(AppRoutes.HomeScreen.route)
    {
        HomeScreen(onNavigate = {
                destination ->
            when (destination) {
                AppRoutes.CheckoutScreen -> navController.navigate(AppRoutes.CheckoutScreen.route)
                AppRoutes.ProfileScreen -> navController.navigate(AppRoutes.ProfileScreen.route)
                AppRoutes.LogoutScreen -> navController.navigate(AppRoutes.LogoutScreen.route)
                else -> Unit
            }
        })
    }


    composable(AppRoutes.CheckoutScreen.route) {
        CheckoutScreen(onNavigate = {
            navController.navigate(AppRoutes.ConfirmationScreen.route)

        })
    }

    composable(AppRoutes.ConfirmationScreen.route) {
        ConfirmationScreen(onNavigate = {
            // Optional: return to Home without growing the stack
            navController.navigate(AppRoutes.HomeScreen.route) {
                popUpTo(AppRoutes.HomeScreen.route) { inclusive = true }
                launchSingleTop = true
            }

        })
    }


    composable(AppRoutes.ProfileScreen.route) {
        ProfileScreen(onNavigate = {
            navController.navigate(AppRoutes.HomeScreen.route) {
                popUpTo(AppRoutes.HomeScreen.route) { inclusive = true }
                launchSingleTop = true
            }

        })
    }

    composable(AppRoutes.LogoutScreen.route) {
        LogoutScreen(onNavigate = {
            // Best practice:
            // on logout, clear the \"main\" stack so user can't go back into Home/Profile/etc.
            navController.navigate(AppRoutes.LoginScreen.route) {
                popUpTo(AppRoutes.HomeScreen.route) { inclusive = true }
                launchSingleTop = true
            }

        })
    }
}


}