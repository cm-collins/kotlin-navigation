package com.example.screen_navigation.ui.theme.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



@Composable
fun MainNavGraph ()
{
    //step 1  ----> Navcontroller
    val navController = rememberNavController ()

    //step 2 -----> Navhost

    NavHost ( navController = navController, startDestination =  "A")
    {
        // step 3 -----> Declare your  screens (route,  composable screen  , params (NavController)
        composable ("A") {
            ScreenA(navController)
        }
        composable ("B") {
            ScreenB(navController)
        }
        composable ("C") {
            ScreenC(navController)
        }

    }
}