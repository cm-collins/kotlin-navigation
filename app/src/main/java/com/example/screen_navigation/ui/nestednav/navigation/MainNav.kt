package com.example.screen_navigation.ui.nestednav.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


@Composable
fun MainAppNav(modifier: Modifier = Modifier) {
    //step 1 = remeber navcontroller
    val navController = rememberNavController()

    //step 2 = Navhost
    NavHost(navController, startDestination = AppRoutes.Auth.route) {
        authGraph(navController)
        homeNavGraph(navController)


    }


}