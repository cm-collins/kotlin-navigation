package com.example.screen_navigation.ui.navargs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun AppNavArgs(modifier: Modifier = Modifier) {
    // step 1: NavController
    val navController = rememberNavController()

    // step 2: NavHost
    NavHost(navController = navController, startDestination = "Home") {

        // step 3: Declare your screens
        composable("Home") {
            HomeScreen(onNavigate = { name, score ->
                // This navigates to the route defined below
                navController.navigate("profile/$name/$score")
            })
        }

        composable(
            route = "profile/{name}/{score}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("score") { type = NavType.StringType }
            )

        ) {

            ProfileScreen(
                name = it.arguments?.getString("name"),
                score  = it.arguments?.getString("score")
            )
        }
    }
}