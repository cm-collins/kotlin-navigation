package com.example.screen_navigation.ui.theme.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * TYPE-SAFE ROUTE DEFINITIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * Using a sealed class for routes provides:
 *
 * 1. COMPILE-TIME SAFETY:
 *    - Typos are caught at compile time, not runtime
 *    - IDE autocomplete for route names
 *
 * 2. SINGLE SOURCE OF TRUTH:
 *    - All routes defined in one place
 *    - Easy to see all available destinations
 *
 * 3. REFACTORING SUPPORT:
 *    - Rename a route and all usages update automatically
 *    - Find all usages of a route with IDE
 *
 * SEALED CLASS vs ENUM:
 * - Sealed class allows each object to have different properties
 * - Can add helper functions (like createRoute for arguments)
 * - More flexible for complex routing scenarios
 */
sealed class Route(val route: String) {
    data object ScreenA : Route(route = "screen_a")
    data object ScreenB : Route(route = "screen_b")
    data object ScreenC : Route(route = "screen_c")
}

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * MAIN NAVIGATION GRAPH - Basic Navigation Without Arguments
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * This NavGraph demonstrates:
 * 1. Basic screen-to-screen navigation
 * 2. Type-safe routing with sealed class
 * 3. Callback pattern for screen navigation
 * 4. popUpTo for back stack management
 *
 * NAVIGATION FLOW:
 * ┌────────────────────────────────────────────────────────────────────────────┐
 * │                                                                            │
 * │    ┌──────────┐  navigate()  ┌──────────┐  navigate()  ┌──────────┐       │
 * │    │ Screen A │ ───────────► │ Screen B │ ───────────► │ Screen C │       │
 * │    └──────────┘              └──────────┘              └──────────┘       │
 * │         ▲                                                    │            │
 * │         │                                                    │            │
 * │         └────────────────────────────────────────────────────┘            │
 * │                        navigate() with popUpTo                            │
 * │                        (clears back stack)                                │
 * │                                                                            │
 * └────────────────────────────────────────────────────────────────────────────┘
 *
 * THREE STEPS TO SET UP NAVIGATION:
 * ════════════════════════════════
 *
 * STEP 1: Create NavController
 *         - Use rememberNavController() to create and remember the controller
 *         - This survives recomposition
 *
 * STEP 2: Set up NavHost
 *         - Container that hosts your navigation graph
 *         - Specify the startDestination (first screen shown)
 *
 * STEP 3: Define composable destinations
 *         - Each composable() defines a screen with its route
 *         - Pass callbacks for navigation actions
 */
@Composable
fun MainNavGraph() {

    // ══════════════════════════════════════════════════════════════════════
    // STEP 1: Create NavController
    // ══════════════════════════════════════════════════════════════════════
    // NavController is the central API for navigation in Compose.
    // - Tracks back stack of composable destinations
    // - Enables navigation between screens
    // - rememberNavController() ensures it survives recomposition
    val navController = rememberNavController()

    // ══════════════════════════════════════════════════════════════════════
    // STEP 2: Set up NavHost
    // ══════════════════════════════════════════════════════════════════════
    // NavHost is the container that displays the current destination.
    // - navController: the controller managing this graph
    // - startDestination: the first screen shown when app launches
    NavHost(
        navController = navController,
        startDestination = Route.ScreenA.route  // Type-safe route reference
    ) {

        // ══════════════════════════════════════════════════════════════════
        // STEP 3: Define Composable Destinations
        // ══════════════════════════════════════════════════════════════════

        // ──────────────────────────────────────────────────────────────────
        // SCREEN A - Starting destination
        // ──────────────────────────────────────────────────────────────────
        composable(route = Route.ScreenA.route) {
            ScreenA(
                onNavigate = {
                    // Simple navigation - adds ScreenB to back stack
                    navController.navigate(Route.ScreenB.route)
                }
            )
        }

        // ──────────────────────────────────────────────────────────────────
        // SCREEN B - Middle screen
        // ──────────────────────────────────────────────────────────────────
        composable(route = Route.ScreenB.route) {
            ScreenB(
                onNavigate = {
                    // Simple navigation - adds ScreenC to back stack
                    navController.navigate(Route.ScreenC.route)
                }
            )
        }

        // ──────────────────────────────────────────────────────────────────
        // SCREEN C - Demonstrates popUpTo navigation
        // ──────────────────────────────────────────────────────────────────
        composable(route = Route.ScreenC.route) {
            ScreenC(
                onNavigate = {
                    // ══════════════════════════════════════════════════════
                    // ADVANCED: popUpTo Navigation
                    // ══════════════════════════════════════════════════════
                    // This clears the back stack when navigating to Screen A
                    //
                    // Without popUpTo:
                    //   Back stack: A → B → C → A → B → C → A...
                    //   User pressing back goes through ALL screens
                    //
                    // With popUpTo(ScreenA) + inclusive = true:
                    //   Back stack: A (fresh start)
                    //   User pressing back exits the app
                    //
                    // inclusive = true: Also removes the target destination
                    //   before adding the new one (prevents duplicate)
                    navController.navigate(Route.ScreenA.route) {
                        popUpTo(Route.ScreenA.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

