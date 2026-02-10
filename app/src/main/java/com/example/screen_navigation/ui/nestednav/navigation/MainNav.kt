package com.example.screen_navigation.ui.nestednav.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screen_navigation.ui.nestednav.screens.CheckoutScreen
import com.example.screen_navigation.ui.nestednav.screens.ConfirmationScreen
import com.example.screen_navigation.ui.nestednav.screens.HomeScreen
import com.example.screen_navigation.ui.nestednav.screens.LoginScreen
import com.example.screen_navigation.ui.nestednav.screens.LogoutScreen
import com.example.screen_navigation.ui.nestednav.screens.ProfileScreen
import com.example.screen_navigation.ui.nestednav.screens.ResetPinScreen
import com.example.screen_navigation.ui.nestednav.screens.SignupScreen


@Composable
fun MainAppNav(modifier: Modifier = Modifier) {
    //step 1 = remeber navcontroller
    val navController = rememberNavController()

    //step 2 = Navhost
    NavHost(navController, startDestination = AppRoutes.LoginScreen.route) {

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