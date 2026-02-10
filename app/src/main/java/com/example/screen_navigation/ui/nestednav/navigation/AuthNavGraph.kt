package com.example.screen_navigation.ui.nestednav.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.screen_navigation.ui.nestednav.screens.LoginScreen
import com.example.screen_navigation.ui.nestednav.screens.ResetPinScreen
import com.example.screen_navigation.ui.nestednav.screens.SignupScreen

fun NavGraphBuilder.authGraph (navController: NavController)

{
    navigation(startDestination = AppRoutes.LoginScreen.route, route = AppRoutes.Auth.route )
    {
        //declare composable screens
        //authentication
        composable(AppRoutes.LoginScreen.route) {
            LoginScreen(onNavigate = {
                // Best practice:
                // after successful login, remove auth screen from back stack
                // so back press doesn't return user to Login.
                navController.navigate(AppRoutes.HomeScreen.route) {
                    popUpTo(AppRoutes.LoginScreen.route) { inclusive = true }
                    launchSingleTop = true
                }

            })
        }

        composable(AppRoutes.SignupScreen.route) {
            SignupScreen(onNavigate = {
                navController.navigate(AppRoutes.LoginScreen.route)

            })
        }


        composable(AppRoutes.ResetPinScreen.route) {
            ResetPinScreen(onNavigate = {
                navController.navigate(AppRoutes.LoginScreen.route)

            })
        }

    }
}
