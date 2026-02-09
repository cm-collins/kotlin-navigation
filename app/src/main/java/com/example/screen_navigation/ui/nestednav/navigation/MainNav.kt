package com.example.screen_navigation.ui.nestednav.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screen_navigation.ui.nestednav.screens.CheckoutScreen
import com.example.screen_navigation.ui.nestednav.screens.HomeScreen
import com.example.screen_navigation.ui.nestednav.screens.LoginScreen
import com.example.screen_navigation.ui.nestednav.screens.ProfileScreen
import com.example.screen_navigation.ui.nestednav.screens.ResetPinScreen
import com.example.screen_navigation.ui.nestednav.screens.SignupScreen


@Composable
fun MainAppNav (modifier: Modifier = Modifier)
{
    //step 1 = remeber navcontroller
    val navController = rememberNavController()

    //step 2 = Navhost
    NavHost(navController, startDestination = AppRoutes.LoginScreen.route) {

        //declare composable screens
        //authentication
        composable ( AppRoutes.LoginScreen.route) {
            LoginScreen( onNavigate = {
                navController.navigate(AppRoutes.HomeScreen.route)

            } )
        }

        composable ( AppRoutes.SignupScreen.route) {
            SignupScreen ( onNavigate = {
                navController.navigate(AppRoutes.LoginScreen.route)

            } )
        }


        composable ( AppRoutes.ResetPinScreen.route) {
            ResetPinScreen ( onNavigate = {
                navController.navigate(AppRoutes.LoginScreen.route)

            } )
        }

        //home screens
        composable(AppRoutes.HomeScreen.route)
        {
            HomeScreen (onNavigate = {})
        }


        composable ( AppRoutes.CheckoutScreen.route) {
            CheckoutScreen ( onNavigate = {
                navController.navigate(AppRoutes.ConfirmationScreen.route)

            } )
        }


        composable ( AppRoutes.ProfileScreen.route) {
            ProfileScreen ( onNavigate = {
                navController.navigate(AppRoutes.HomeScreen.route)

            } )
        }




    }


}