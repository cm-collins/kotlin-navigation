# Jetpack Compose Navigation Guide

> A comprehensive guide to implementing navigation in Jetpack Compose - from basic screen-to-screen navigation to passing data with arguments.

---

## Table of Contents

1. [Overview](#overview)
2. [Project Structure](#project-structure)
3. [Core Concepts](#core-concepts)
4. [Part 1: Basic Navigation](#part-1-basic-navigation-screens-folder)
5. [Part 2: Navigation with Arguments](#part-2-navigation-with-arguments-navargs-folder)
6. [Part 3: Multi-flow Navigation (Auth + Main)](#part-3-multi-flow-navigation-auth--main-nestednav-folder)
7. [Type-Safe Routing](#type-safe-routing)
8. [State Management](#state-management)
9. [Navigation Flow Diagrams](#navigation-flow-diagrams)
10. [Best Practices](#best-practices)
11. [Common Patterns](#common-patterns)

---

## Overview

This project demonstrates **Jetpack Compose Navigation** through three implementations:

1. **Basic Navigation** (`screens/`) - Simple screen-to-screen navigation with `popUpTo`
2. **Navigation with Arguments** (`navargs/`) - Passing data between screens
3. **Multi-flow Navigation** (`nestednav/`) - Auth flow + Main flow in one app graph

Both implementations use the `navigation-compose` library with **type-safe routing** using sealed classes.

### Key Features Implemented

- ✅ NavController for navigation management
- ✅ NavHost for hosting navigation graph
- ✅ Type-safe routing with sealed classes (no string literals!)
- ✅ Callback pattern for screen navigation (decoupled architecture)
- ✅ Navigation arguments for passing data
- ✅ `popUpTo` for back stack management
- ✅ State management with `remember` and `mutableStateOf`

---

## Project Structure

```
app/src/main/java/com/example/screen_navigation/
│
├── MainActivity.kt                    # Entry point - sets up theme
│
└── ui/
    ├── screens/                       # BASIC NAVIGATION (no arguments)
    │   ├── NavGraph.kt               # Navigation graph with Route sealed class
    │   ├── ScreenA.kt                # First screen → navigates to B
    │   ├── ScreenB.kt                # Second screen → navigates to C
    │   └── ScreenC.kt                # Third screen → navigates to A (with popUpTo)
    │
    ├── navargs/                       # NAVIGATION WITH ARGUMENTS
    │   ├── NavArgsGraph.kt           # Navigation graph with argument routes
    │   ├── HomeScreen.kt             # Input screen (TextField + state)
    │   └── ProfileScreen.kt          # Display screen (receives arguments)
    │
    ├── nestednav/                     # MULTI-FLOW NAVIGATION (Auth + Main)
    │   ├── navigation/
    │   │   ├── MainNav.kt            # MainAppNav NavHost (single graph)
    │   │   └── NavRoutes.kt          # AppRoutes sealed class
    │   └── screens/
    │       ├── AuthScreen.kt         # Login / Signup / Reset PIN
    │       └── HomeScreen.kt         # Home / Checkout / Confirmation / Logout (demo)
    │
    └── theme/                         # Material 3 theming
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

---

## Architecture Diagram

### High-Level Navigation Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                           MainActivity                               │
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │                    ScreennavigationTheme                       │  │
│  │                                                                │  │
│  │  ┌─────────────────────────────────────────────────────────┐  │  │
│  │  │        NavGraph (screens/ OR navargs/ OR nestednav/)     │  │  │
│  │  │  ┌────────────────────────────────────────────────────┐ │  │  │
│  │  │  │                   NavController                     │ │  │  │
│  │  │  │         (manages navigation state)                  │ │  │  │
│  │  │  └────────────────────────────────────────────────────┘ │  │  │
│  │  │                          │                               │  │  │
│  │  │                          ▼                               │  │  │
│  │  │  ┌────────────────────────────────────────────────────┐ │  │  │
│  │  │  │                     NavHost                         │ │  │  │
│  │  │  │            (hosts composable screens)               │ │  │  │
│  │  │  └────────────────────────────────────────────────────┘ │  │  │
│  │  └─────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
```

### Component Relationship Diagram

```
┌──────────────────────────────────────────────────────────────────────┐
│                        NAVIGATION COMPONENTS                          │
└──────────────────────────────────────────────────────────────────────┘

     ┌─────────────────┐
     │  NavController  │◄──────── Created with rememberNavController()
     │                 │
     │  • navigate()   │          Controls navigation between screens
     │  • popBackStack │          Maintains back stack
     │  • currentRoute │          Holds current destination info
     └────────┬────────┘
              │
              │ passed to
              ▼
     ┌─────────────────┐
     │    NavHost      │◄──────── Container for navigation graph
     │                 │
     │  • navController│          Defines all possible destinations
     │  • startDest    │          Specifies starting screen
     │  • builder {}   │          Contains composable() definitions
     └────────┬────────┘
              │
              │ contains
              ▼
     ┌─────────────────┐
     │  composable()   │◄──────── Defines individual screen destinations
     │                 │
     │  • route        │          URL-like path with optional args
     │  • arguments    │          List of navArgument definitions
     │  • content {}   │          Actual composable UI content
     └─────────────────┘
```

---

## Core Concepts

### 1. NavController

The **NavController** is the central API for navigation in Compose. It:

- Tracks the back stack of composable destinations
- Enables forward navigation via `navigate()`
- Supports back navigation via `popBackStack()`
- Maintains navigation state across recompositions

```kotlin
val navController = rememberNavController()
```

> 💡 **Note:** Use `rememberNavController()` to create a NavController that survives recomposition.

---

### 2. NavHost

The **NavHost** is a composable that:

- Hosts the navigation graph
- Displays the current destination
- Handles transitions between screens

```kotlin
NavHost(
    navController = navController,
    startDestination = Route.Home.route  // Type-safe!
) {
    // Screen definitions go here
}
```

---

### 3. Routes

Routes are **string-based paths** that identify destinations. They can include:

- **Static routes:** `"home"`, `"settings"`
- **Dynamic routes with arguments:** `"profile/{name}/{score}"`

```
Route Pattern Examples:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Static Route     │  "home"
  ─────────────────┼─────────────────────────────
  With 1 Argument  │  "profile/{userId}"
  ─────────────────┼─────────────────────────────
  With 2 Arguments │  "profile/{name}/{score}"
  ─────────────────┼─────────────────────────────
  Optional Args    │  "search?query={query}"
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

### 4. Callback Pattern for Navigation

Instead of passing `NavController` directly to screens, we use **callbacks**:

```kotlin
// ✅ GOOD: Callback pattern (decoupled)
@Composable
fun ScreenA(onNavigate: () -> Unit = {}) {
    Button(onClick = { onNavigate() }) {
        Text("Next")
    }
}

// ❌ AVOID: Direct NavController (tight coupling)
@Composable
fun ScreenA(navController: NavController) {
    Button(onClick = { navController.navigate("B") }) {
        Text("Next")
    }
}
```

**Benefits of Callback Pattern:**

- Screen doesn't know about navigation implementation
- Enables preview without NavController
- Easier to test in isolation
- Navigation logic centralized in NavGraph

---

## Part 1: Basic Navigation (screens/ folder)

This implementation demonstrates simple screen-to-screen navigation with `popUpTo` for back stack management.

### Navigation Flow

```
┌────────────────────────────────────────────────────────────────────────────┐
│                     BASIC NAVIGATION FLOW (A → B → C → A)                  │
└────────────────────────────────────────────────────────────────────────────┘

    ┌──────────┐  navigate()  ┌──────────┐  navigate()  ┌──────────┐
    │ Screen A │ ───────────► │ Screen B │ ───────────► │ Screen C │
    └──────────┘              └──────────┘              └──────────┘
         ▲                                                    │
         │                                                    │
         └────────────────────────────────────────────────────┘
                        navigate() with popUpTo
                        (clears back stack)
```

### Type-Safe Route Definition (NavGraph.kt)

```kotlin
/**
 * Sealed class for type-safe routes.
 * - Compile-time safety (typos caught by compiler)
 * - IDE autocomplete support
 * - Single source of truth for all routes
 */
sealed class Route(val route: String) {
    data object ScreenA : Route(route = "screen_a")
    data object ScreenB : Route(route = "screen_b")
    data object ScreenC : Route(route = "screen_c")
}
```

### Navigation Graph Setup

```kotlin
@Composable
fun MainNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.ScreenA.route  // Type-safe!
    ) {
        // Screen A → B
        composable(route = Route.ScreenA.route) {
            ScreenA(
                onNavigate = {
                    navController.navigate(Route.ScreenB.route)
                }
            )
        }

        // Screen B → C
        composable(route = Route.ScreenB.route) {
            ScreenB(
                onNavigate = {
                    navController.navigate(Route.ScreenC.route)
                }
            )
        }

        // Screen C → A (with popUpTo to clear stack)
        composable(route = Route.ScreenC.route) {
            ScreenC(
                onNavigate = {
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
```

### Understanding popUpTo

```
┌─────────────────────────────────────────────────────────────────────┐
│                         popUpTo BEHAVIOR                             │
└─────────────────────────────────────────────────────────────────────┘

  WITHOUT popUpTo:                    WITH popUpTo + inclusive:
  ─────────────────────────────       ─────────────────────────────

  Back stack keeps growing:           Back stack stays clean:

  ┌──────────┐                        ┌──────────┐
  │ Screen A │ (4th)                  │ Screen A │ (fresh start)
  ├──────────┤                        └──────────┘
  │ Screen C │
  ├──────────┤                        User presses back → exits app
  │ Screen B │                        (expected behavior!)
  ├──────────┤
  │ Screen A │ (1st)
  └──────────┘

  User presses back → goes through
  ALL screens (bad UX!)
```

### Screen Composables (ScreenA, ScreenB, ScreenC)

All screens follow the same pattern:

```kotlin
@Composable
fun ScreenA(onNavigate: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Screen A", fontSize = 33.sp)

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = { onNavigate() }) {
            Text("Go to Screen B")
        }
    }
}
```

**Key Points:**

- `onNavigate: () -> Unit = {}` - Callback with default empty lambda
- Default lambda enables Preview without navigation
- Screen is completely decoupled from navigation logic

---

## Part 2: Navigation with Arguments (navargs/ folder)

This implementation demonstrates passing data between screens using navigation arguments.

### Type-Safe Route with Arguments

```kotlin
sealed class NavArgsRoute(val route: String) {

    // Static route - no arguments
    data object Home : NavArgsRoute(route = "home")

    // Dynamic route with arguments
    data object Profile : NavArgsRoute(route = "profile/{name}/{score}") {
        // Argument keys as constants (prevents typos!)
        const val ARG_NAME = "name"
        const val ARG_SCORE = "score"

        // Helper function to build route with actual values
        fun createRoute(name: String, score: String): String {
            return "profile/$name/$score"
        }
    }
}
```

### Navigation Graph with Arguments

```kotlin
@Composable
fun AppNavArgs(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavArgsRoute.Home.route
    ) {
        // Home Screen
        composable(route = NavArgsRoute.Home.route) {
            HomeScreen(
                onNavigate = { name, score ->
                    // Type-safe navigation with helper function
                    navController.navigate(
                        NavArgsRoute.Profile.createRoute(name, score)
                    )
                }
            )
        }

        // Profile Screen with arguments
        composable(
            route = NavArgsRoute.Profile.route,
            arguments = listOf(
                navArgument(NavArgsRoute.Profile.ARG_NAME) {
                    type = NavType.StringType
                },
                navArgument(NavArgsRoute.Profile.ARG_SCORE) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            ProfileScreen(
                name = backStackEntry.arguments?.getString(
                    NavArgsRoute.Profile.ARG_NAME
                ),
                score = backStackEntry.arguments?.getString(
                    NavArgsRoute.Profile.ARG_SCORE
                )
            )
        }
    }
}
```

### Supported NavType Values

| NavType              | Kotlin Type | Example      |
| -------------------- | ----------- | ------------ |
| `NavType.StringType` | String      | `"John"`     |
| `NavType.IntType`    | Int         | `42`         |
| `NavType.LongType`   | Long        | `123456789L` |
| `NavType.FloatType`  | Float       | `3.14f`      |
| `NavType.BoolType`   | Boolean     | `true`       |

---

## Part 3: Multi-flow Navigation (Auth + Main) (nestednav/ folder)

This implementation models a more realistic app: **an authentication flow + a main app flow**.

### What “multi-flow” means

Instead of just A → B → C, you often have flows like:
- **Auth flow**: Login, Signup, Reset PIN
- **Main flow**: Home, Profile, Checkout, Confirmation, Logout

In your code, this is implemented in `ui/nestednav/navigation/MainNav.kt` via `MainAppNav()`.

### Routes (type-safe)

Routes are centralized in a sealed class:

```kotlin
sealed class AppRoutes(val route: String) {
    data object HomeScreen : AppRoutes(route = "Home")
    data object CheckoutScreen : AppRoutes(route = "checkout")
    data object ProfileScreen : AppRoutes(route = "profile")
    data object ConfirmationScreen : AppRoutes(route = "confirmation")
    data object LoginScreen : AppRoutes(route = "login")
    data object SignupScreen : AppRoutes(route = "sign_up")
    data object ResetPinScreen : AppRoutes(route = "reset_pin")
    data object LogoutScreen : AppRoutes(route = "logout")
}
```

### Navigation graph (single NavHost for the demo)

`MainAppNav()` uses one `NavHost` with `startDestination = login`.

Important best-practice detail that’s implemented:
- **After login**, navigate to Home using `popUpTo(login) { inclusive = true }`
  - prevents back-press from returning to Login
- **After logout**, navigate to Login using `popUpTo(home) { inclusive = true }`
  - prevents back-press from returning to Home/Profile/Checkout

### Flow diagram

```
┌───────────────────────────────────────────────────────────────────────────┐
│                           MULTI-FLOW NAVIGATION                           │
└───────────────────────────────────────────────────────────────────────────┘

AUTH FLOW                               MAIN FLOW
─────────                               ─────────

  login  ───────────────►  home  ───────►  checkout  ───────► confirmation
    │                      │   │                            │
    │                      │   └────────► profile ──────────┘
    │                      │
    │                      └────────► logout  ─────────────► login
    │
    ├────────► sign_up ───────────────► login
    └────────► reset_pin ─────────────► login
```

### “Modern callback” for destination selection

Your `nestednav` `HomeScreen` uses a more scalable callback:

- Instead of `onNavigate()` (single action),
- it uses `onNavigate(destination: AppRoutes)` (multiple actions).

This keeps **NavController in the NavGraph** and keeps UI screens decoupled.

### Recommended next step (true nested graphs)

Right now, `nestednav` is “multi-flow” but still a **single NavHost**.
In larger apps, you typically create **nested graphs**:

```kotlin
NavHost(navController, startDestination = "auth") {
    navigation(startDestination = AppRoutes.LoginScreen.route, route = "auth") {
        composable(AppRoutes.LoginScreen.route) { /* ... */ }
        composable(AppRoutes.SignupScreen.route) { /* ... */ }
        composable(AppRoutes.ResetPinScreen.route) { /* ... */ }
    }
    navigation(startDestination = AppRoutes.HomeScreen.route, route = "main") {
        composable(AppRoutes.HomeScreen.route) { /* ... */ }
        composable(AppRoutes.ProfileScreen.route) { /* ... */ }
        composable(AppRoutes.CheckoutScreen.route) { /* ... */ }
        composable(AppRoutes.ConfirmationScreen.route) { /* ... */ }
        composable(AppRoutes.LogoutScreen.route) { /* ... */ }
    }
}
```

This makes it easier to:
- keep Auth + Main flows separate
- clear a whole flow using `popUpTo("auth")` / `popUpTo("main")`
- scale to more features

---

## Type-Safe Routing

### Why Use Sealed Classes for Routes?

```
┌─────────────────────────────────────────────────────────────────────┐
│                   STRING LITERALS vs SEALED CLASS                    │
└─────────────────────────────────────────────────────────────────────┘

  ❌ STRING LITERALS (error-prone):
  ─────────────────────────────────
  navController.navigate("profil/John/95")  // Typo: "profil"
                                            // Compiles but crashes at runtime!

  ✅ SEALED CLASS (type-safe):
  ─────────────────────────────────
  navController.navigate(Route.Profile.createRoute("John", "95"))
                         // Typo would be caught by compiler!
```

### Benefits of Sealed Class Routes

| Benefit                    | Description                                 |
| -------------------------- | ------------------------------------------- |
| **Compile-time safety**    | Typos caught by compiler, not at runtime    |
| **IDE autocomplete**       | `Route.` shows all available routes         |
| **Single source of truth** | All routes defined in one place             |
| **Easy refactoring**       | Rename route, all usages update             |
| **Argument constants**     | `ARG_NAME` instead of `"name"` strings      |
| **Helper functions**       | `createRoute()` for building dynamic routes |

---

## State Management

### remember + mutableStateOf Pattern

```kotlin
@Composable
fun HomeScreen(onNavigate: (String, String) -> Unit = { _, _ -> }) {
    // State that survives recomposition
    var name by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }

    // When name or score changes, only affected UI recomposes
    TextField(
        value = name,
        onValueChange = { name = it }  // Updates state → triggers recomposition
    )
}
```

### State Flow Diagram

```
┌────────────────────────────────────────────────────────────────┐
│                    STATE FLOW IN HOMESCREEN                     │
└────────────────────────────────────────────────────────────────┘

  ┌─────────────────────────────────────────────────────────────┐
  │  HomeScreen                                                  │
  │                                                              │
  │   ┌─────────────────┐      ┌─────────────────┐              │
  │   │  name: State    │      │  score: State   │              │
  │   │  (mutableState) │      │  (mutableState) │              │
  │   └────────┬────────┘      └────────┬────────┘              │
  │            │                        │                        │
  │            ▼                        ▼                        │
  │   ┌─────────────────────────────────────────────────────┐   │
  │   │              TextField Components                    │   │
  │   │                                                      │   │
  │   │   value = name          value = score               │   │
  │   │   onValueChange = {}    onValueChange = {}          │   │
  │   └─────────────────────────────────────────────────────┘   │
  │                              │                               │
  │                              ▼                               │
  │   ┌─────────────────────────────────────────────────────┐   │
  │   │                    Button                            │   │
  │   │                                                      │   │
  │   │   onClick = { onNavigate(name, score) }             │   │
  │   └─────────────────────────────────────────────────────┘   │
  │                              │                               │
  └──────────────────────────────┼───────────────────────────────┘
                                 │
                                 ▼  Callback to parent (NavGraph)
                     ┌─────────────────────────────┐
                     │ navController.navigate(     │
                     │   Route.Profile.createRoute │
                     │     (name, score)           │
                     │ )                           │
                     └─────────────────────────────┘
```

---

## Navigation Flow Diagrams

### Complete Data Flow (Navigation with Arguments)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         NAVIGATION DATA FLOW                                 │
└─────────────────────────────────────────────────────────────────────────────┘

  USER INPUT                    NAVIGATION                      DESTINATION
  ──────────────────────────    ──────────────────────────      ──────────────

  ┌───────────────────────┐
  │      HomeScreen       │
  │  ┌─────────────────┐  │
  │  │ TextField: name │──┼──► name = "John"
  │  └─────────────────┘  │
  │  ┌─────────────────┐  │
  │  │ TextField: score│──┼──► score = "95"
  │  └─────────────────┘  │
  │  ┌─────────────────┐  │
  │  │  Button: Click  │──┼──┐
  │  └─────────────────┘  │  │
  └───────────────────────┘  │
                             │
                             ▼
              ┌──────────────────────────────┐
              │    onNavigate("John", "95")  │
              └──────────────┬───────────────┘
                             │
                             ▼
              ┌──────────────────────────────┐
              │  Route.Profile.createRoute(  │
              │    "John", "95"              │
              │  ) → "profile/John/95"       │
              └──────────────┬───────────────┘
                             │
                             ▼
              ┌──────────────────────────────┐
              │  Route Matching:             │
              │  "profile/{name}/{score}"    │
              │                              │
              │  Extracted:                  │
              │    ARG_NAME  = "John"        │
              │    ARG_SCORE = "95"          │
              └──────────────┬───────────────┘
                             │
                             ▼
              ┌───────────────────────┐
              │     ProfileScreen     │
              │  ┌─────────────────┐  │
              │  │ Name: John      │  │
              │  └─────────────────┘  │
              │  ┌─────────────────┐  │
              │  │ Score: 95       │  │
              │  └─────────────────┘  │
              └───────────────────────┘
```

### Back Stack Visualization

```
                        BACK STACK
                    ─────────────────

  Initial State:        After Navigation:
  ┌─────────────┐       ┌─────────────┐
  │             │       │ Profile     │ ◄── Current
  │             │       │ Screen      │
  │             │       ├─────────────┤
  │             │       │ Home        │
  │   Home      │ ───►  │ Screen      │
  │   Screen    │       │             │
  │             │       │             │
  └─────────────┘       └─────────────┘

                        Back Press:
                        ┌─────────────┐
                        │             │
                        │   Home      │ ◄── Current
                        │   Screen    │     (Profile popped)
                        │             │
                        └─────────────┘
```

---

## Best Practices

### 1. Use Sealed Classes for Type-Safe Routes

```kotlin
// ✅ BEST: Sealed class with helper functions
sealed class Route(val route: String) {
    data object Home : Route(route = "home")

    data object Profile : Route(route = "profile/{name}/{score}") {
        const val ARG_NAME = "name"
        const val ARG_SCORE = "score"

        fun createRoute(name: String, score: String) = "profile/$name/$score"
    }
}

// Usage: Route.Profile.createRoute("John", "95")
```

### 2. Use Callback Patterns for Navigation

```kotlin
// ✅ GOOD: Pass navigation as callback (decoupled)
@Composable
fun ScreenA(onNavigate: () -> Unit = {}) {
    Button(onClick = { onNavigate() }) { Text("Next") }
}

// ❌ AVOID: Passing NavController directly (tight coupling)
@Composable
fun ScreenA(navController: NavController) {
    Button(onClick = { navController.navigate("B") }) { Text("Next") }
}
```

### 3. Handle Nullable Arguments Safely

```kotlin
// ✅ Safe handling with Elvis operator
ProfileScreen(
    name = backStackEntry.arguments?.getString(Route.Profile.ARG_NAME)
        ?: "Unknown",
    score = backStackEntry.arguments?.getString(Route.Profile.ARG_SCORE)
        ?: "0"
)
```

### 4. Use Default Parameter Values for Previews

```kotlin
// ✅ Allows preview and testing without navigation setup
@Composable
fun HomeScreen(onNavigate: (String, String) -> Unit = { _, _ -> }) {
    // Preview works because default lambda does nothing
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()  // Uses default empty lambda
}
```

### 5. Use popUpTo for Circular Navigation

```kotlin
// ✅ Prevents infinite back stack growth
navController.navigate(Route.ScreenA.route) {
    popUpTo(Route.ScreenA.route) {
        inclusive = true  // Remove existing ScreenA before adding new one
    }
}
```

---

## Common Patterns

### Optional Arguments

```kotlin
data object Search : Route(route = "search?query={query}") {
    const val ARG_QUERY = "query"

    fun createRoute(query: String? = null) =
        if (query != null) "search?query=$query" else "search"
}

// In NavHost:
composable(
    route = Route.Search.route,
    arguments = listOf(
        navArgument(Route.Search.ARG_QUERY) {
            type = NavType.StringType
            defaultValue = ""
            nullable = true
        }
    )
) { }
```

### Deep Links

```kotlin
composable(
    route = Route.Profile.route,
    deepLinks = listOf(
        navDeepLink {
            uriPattern = "https://example.com/profile/{name}/{score}"
        }
    )
) { }
```

### Nested Navigation

```kotlin
sealed class MainRoute(val route: String) {
    data object Main : MainRoute("main")
    data object Auth : MainRoute("auth")
}

NavHost(navController, startDestination = MainRoute.Main.route) {
    navigation(startDestination = Route.Home.route, route = MainRoute.Main.route) {
        composable(Route.Home.route) { HomeScreen() }
        composable(Route.Settings.route) { SettingsScreen() }
    }
    navigation(startDestination = Route.Login.route, route = MainRoute.Auth.route) {
        composable(Route.Login.route) { LoginScreen() }
        composable(Route.Register.route) { RegisterScreen() }
    }
}
```

---

## Quick Reference

### Navigation Setup Checklist

```
┌─────────────────────────────────────────────────────────────────┐
│                    NAVIGATION SETUP CHECKLIST                    │
└─────────────────────────────────────────────────────────────────┘

  □ Step 1: Define sealed class for routes
  □ Step 2: Create NavController with rememberNavController()
  □ Step 3: Setup NavHost with startDestination
  □ Step 4: Define composable() for each screen
  □ Step 5: Use callbacks (onNavigate) in screens
  □ Step 6: Add arguments with navArgument() if needed
  □ Step 7: Use popUpTo for circular navigation flows
```

### Summary Table

| Concept                | Purpose                     | Example                                                  |
| ---------------------- | --------------------------- | -------------------------------------------------------- |
| **Sealed Class Route** | Type-safe route definitions | `sealed class Route(val route: String)`                  |
| **NavController**      | Manages navigation state    | `rememberNavController()`                                |
| **NavHost**            | Hosts navigation graph      | `NavHost(navController, startDestination)`               |
| **composable()**       | Defines screen destination  | `composable(Route.Home.route) { }`                       |
| **navArgument**        | Defines route parameter     | `navArgument(ARG_NAME) { type = NavType.StringType }`    |
| **navigate()**         | Triggers navigation         | `navController.navigate(Route.Profile.createRoute(...))` |
| **popUpTo**            | Clears back stack           | `popUpTo(Route.Home.route) { inclusive = true }`         |
| **Callback Pattern**   | Decouples screens from nav  | `onNavigate: () -> Unit = {}`                            |

---

## Glossary

| Term               | Definition                                             |
| ------------------ | ------------------------------------------------------ |
| **Back Stack**     | Stack of screens the user has navigated through        |
| **Composable**     | A function that defines UI in Jetpack Compose          |
| **NavController**  | Object that manages app navigation                     |
| **NavHost**        | Container that displays current navigation destination |
| **Route**          | String path that identifies a navigation destination   |
| **Recomposition**  | Process of re-executing composables when state changes |
| **State Hoisting** | Moving state up to a common ancestor composable        |
| **popUpTo**        | Navigation option to remove screens from back stack    |

---

## References

- [Official Navigation Compose Guide](https://developer.android.com/jetpack/compose/navigation)
- [Navigation Arguments Documentation](https://developer.android.com/guide/navigation/navigation-pass-data)
- [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state)
- [Material 3 Design](https://m3.material.io/)

---

_Last Updated: January 2026_
