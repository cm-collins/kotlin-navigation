# Jetpack Compose Navigation Guide

> A comprehensive guide to implementing navigation in Jetpack Compose - from basic screen-to-screen navigation, to passing data with arguments, to nested navigation graphs.

---

## Table of Contents

1. [Overview](#overview)
2. [Project Structure](#project-structure)
3. [Core Concepts](#core-concepts)
4. [Part 1: Basic Navigation](#part-1-basic-navigation-screens-folder)
5. [Part 2: Navigation with Arguments](#part-2-navigation-with-arguments-navargs-folder)
6. [Part 3: Nested Navigation Graphs](#part-3-nested-navigation-graphs-nestednav-folder)
7. [Type-Safe Routing](#type-safe-routing)
8. [State Management](#state-management)
9. [Navigation Flow Diagrams](#navigation-flow-diagrams)
10. [Best Practices](#best-practices)
11. [Common Patterns](#common-patterns)

---

## Overview

This project demonstrates **Jetpack Compose Navigation** through three implementations, each building on the previous one:

1. **Basic Navigation** (`screens/`) - Simple screen-to-screen navigation with `popUpTo`
2. **Navigation with Arguments** (`navargs/`) - Passing data between screens via route parameters
3. **Nested Navigation Graphs** (`nestednav/`) - Splitting an Auth flow and Main flow into separate sub-graphs using `NavGraphBuilder` extension functions

All implementations use the `navigation-compose` library with **type-safe routing** using sealed classes.

### Key Features Implemented

- NavController for navigation management
- NavHost for hosting navigation graph
- Type-safe routing with sealed classes (no string literals!)
- Callback pattern for screen navigation (decoupled architecture)
- Navigation arguments for passing data
- `popUpTo` + `launchSingleTop` for back stack management
- Nested navigation graphs with `NavGraphBuilder` extension functions
- Multi-destination callback pattern
- State management with `remember` and `mutableStateOf`

---

## Project Structure

```
app/src/main/java/com/example/screen_navigation/
|
├── MainActivity.kt                    # Entry point - sets up theme
|
└── ui/
    ├── screens/                       # PART 1 - BASIC NAVIGATION
    |   ├── NavGraph.kt               # Navigation graph + Route sealed class
    |   ├── ScreenA.kt                # First screen -> navigates to B
    |   ├── ScreenB.kt                # Second screen -> navigates to C
    |   └── ScreenC.kt                # Third screen -> navigates to A (popUpTo)
    |
    ├── navargs/                       # PART 2 - NAVIGATION WITH ARGUMENTS
    |   ├── NavArgsGraph.kt           # NavHost with argument routes
    |   ├── HomeScreen.kt             # Input screen (TextField + state)
    |   └── ProfileScreen.kt          # Display screen (receives arguments)
    |
    ├── nestednav/                     # PART 3 - NESTED NAVIGATION GRAPHS
    |   ├── navigation/
    |   |   ├── NavRoutes.kt          # AppRoutes sealed class (screens + graph IDs)
    |   |   ├── MainNav.kt            # MainAppNav: thin NavHost shell
    |   |   ├── AuthNavGraph.kt       # NavGraphBuilder.authGraph() extension
    |   |   └── HomeNavGraph.kt       # NavGraphBuilder.homeNavGraph() extension
    |   └── screens/
    |       ├── AuthScreen.kt         # Login / Signup / Reset PIN composables
    |       └── HomeScreen.kt         # Home / Profile / Checkout / Confirm / Logout
    |
    └── theme/                         # Material 3 theming
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
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

> Use `rememberNavController()` to create a NavController that survives recomposition.

---

### 2. NavHost

The **NavHost** is a composable that:

- Hosts the navigation graph
- Displays the current destination
- Handles transitions between screens

```kotlin
NavHost(
    navController = navController,
    startDestination = Route.Home.route
) {
    // Screen definitions go here
}
```

---

### 3. Routes

Routes are **string-based paths** that identify destinations:

```
Route Pattern Examples:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Static Route     |  "home"
  -----------------|-────────────────────────────-
  With 1 Argument  |  "profile/{userId}"
  -----------------|-────────────────────────────-
  With 2 Arguments |  "profile/{name}/{score}"
  -----------------|-────────────────────────────-
  Optional Args    |  "search?query={query}"
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

### 4. Callback Pattern for Navigation

Instead of passing `NavController` directly to screens, we use **callbacks**:

```kotlin
// GOOD: Callback pattern (decoupled)
@Composable
fun ScreenA(onNavigate: () -> Unit = {}) {
    Button(onClick = { onNavigate() }) { Text("Next") }
}

// AVOID: Direct NavController (tight coupling)
@Composable
fun ScreenA(navController: NavController) {
    Button(onClick = { navController.navigate("B") }) { Text("Next") }
}
```

**Benefits of Callback Pattern:**

- Screen doesn't know about navigation implementation
- Enables `@Preview` without NavController
- Easier to test in isolation
- Navigation logic centralized in NavGraph

---

## Part 1: Basic Navigation (screens/ folder)

Simple screen-to-screen navigation with `popUpTo` for back stack management.

### Navigation Flow

```
┌────────────────────────────────────────────────────────────────────────────┐
│                     BASIC NAVIGATION FLOW (A -> B -> C -> A)               │
└────────────────────────────────────────────────────────────────────────────┘

    ┌──────────┐  navigate()  ┌──────────┐  navigate()  ┌──────────┐
    │ Screen A │ -----------> │ Screen B │ -----------> │ Screen C │
    └──────────┘              └──────────┘              └──────────┘
         ^                                                    |
         |                                                    |
         └────────────────────────────────────────────────────┘
                        navigate() with popUpTo
                        (clears back stack)
```

### Type-Safe Route Definition (NavGraph.kt)

```kotlin
sealed class Route(val route: String) {
    data object ScreenA : Route(route = "screen_a")
    data object ScreenB : Route(route = "screen_b")
    data object ScreenC : Route(route = "screen_c")
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
  ├──────────┤                        User presses back -> exits app
  │ Screen B │                        (expected behavior!)
  ├──────────┤
  │ Screen A │ (1st)
  └──────────┘

  User presses back -> goes through
  ALL screens (bad UX!)
```

---

## Part 2: Navigation with Arguments (navargs/ folder)

Passing data between screens using navigation arguments.

### Type-Safe Route with Arguments

```kotlin
sealed class NavArgsRoute(val route: String) {
    data object Home : NavArgsRoute(route = "home")

    data object Profile : NavArgsRoute(route = "profile/{name}/{score}") {
        const val ARG_NAME = "name"
        const val ARG_SCORE = "score"

        fun createRoute(name: String, score: String): String {
            return "profile/$name/$score"
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

## Part 3: Nested Navigation Graphs (nestednav/ folder)

This is the most realistic pattern in the project. It models an app with separate **Auth** and **Main** flows, each defined in its own file using **extension functions on `NavGraphBuilder`**.

---

### What is a Nested Navigation Graph?

A nested graph groups related screens under a single "parent route". Think of it like folders on your computer -- instead of dumping all files in one place, you organize them:

```
┌──────────────────────────────────────────────────────────────────────────┐
│                    FLAT GRAPH vs NESTED GRAPHS                            │
└──────────────────────────────────────────────────────────────────────────┘

  FLAT (all screens in one NavHost block):
  ─────────────────────────────────────────
  NavHost {
      composable("login") { }
      composable("signup") { }
      composable("reset_pin") { }
      composable("home") { }
      composable("checkout") { }
      composable("confirmation") { }
      composable("profile") { }
      composable("logout") { }        <-- 8 screens in one place = messy
  }

  NESTED (grouped by flow, in separate files):
  ─────────────────────────────────────────────
  NavHost(startDestination = "auth") {
      authGraph(navController)         <-- login, signup, reset_pin
      homeNavGraph(navController)      <-- home, checkout, profile, etc.
  }
```

---

### Why Use Nested Graphs?

| Benefit                | Explanation                                                    |
| ---------------------- | -------------------------------------------------------------- |
| **Organization**       | Each flow in its own file -- easy to find and edit             |
| **Scalability**        | Adding a new flow = adding a new file + one line in NavHost   |
| **Back stack control** | `popUpTo("auth")` clears the entire auth group at once        |
| **Team collaboration** | Different devs can work on different flows without conflicts   |
| **Reusability**        | An auth graph can be reused in another app/module              |

---

### How It All Fits Together

```
┌──────────────────────────────────────────────────────────────────────────┐
│                      NESTED GRAPH ARCHITECTURE                            │
└──────────────────────────────────────────────────────────────────────────┘

  ┌──────────────────────────────────────────────────────────────────────┐
  │  MainActivity                                                        │
  │  └── MainAppNav()     (MainNav.kt)                                   │
  │       |                                                              │
  │       v                                                              │
  │  ┌──────────────────────────────────────────────────────────────┐    │
  │  │  NavHost(startDestination = "auth")                          │    │
  │  │  |                                                           │    │
  │  │  ├── authGraph(navController)         (AuthNavGraph.kt)      │    │
  │  │  │   └── navigation(route = "auth")                          │    │
  │  │  │       ├── composable("login")      -> LoginScreen         │    │
  │  │  │       ├── composable("sign_up")    -> SignupScreen        │    │
  │  │  │       └── composable("reset_pin")  -> ResetPinScreen     │    │
  │  │  │                                                           │    │
  │  │  └── homeNavGraph(navController)      (HomeNavGraph.kt)      │    │
  │  │      └── navigation(route = "main")                          │    │
  │  │          ├── composable("Home")       -> HomeScreen          │    │
  │  │          ├── composable("checkout")   -> CheckoutScreen      │    │
  │  │          ├── composable("confirmation")-> ConfirmationScreen │    │
  │  │          ├── composable("profile")    -> ProfileScreen       │    │
  │  │          └── composable("logout")     -> LogoutScreen        │    │
  │  └──────────────────────────────────────────────────────────────┘    │
  └──────────────────────────────────────────────────────────────────────┘
```

---

### File-by-File Breakdown

#### 1. NavRoutes.kt -- All Routes in One Place

```kotlin
sealed class AppRoutes(val route: String) {
    // Screen routes
    data object HomeScreen : AppRoutes(route = "Home")
    data object CheckoutScreen : AppRoutes(route = "checkout")
    data object ProfileScreen : AppRoutes(route = "profile")
    data object ConfirmationScreen : AppRoutes(route = "confirmation")
    data object LoginScreen : AppRoutes(route = "login")
    data object SignupScreen : AppRoutes(route = "sign_up")
    data object ResetPinScreen : AppRoutes(route = "reset_pin")
    data object LogoutScreen : AppRoutes(route = "logout")

    // Graph-level route identifiers (NOT screens)
    data object Auth : AppRoutes("auth")
    data object Main : AppRoutes("main")
}
```

**Key detail:** `Auth` and `Main` are not screens -- they are **graph identifiers** used as the `route` parameter in `navigation()`. They let you say "navigate to the auth graph" or "pop everything up to the main graph".

---

#### 2. MainNav.kt -- The Thin Shell

```kotlin
@Composable
fun MainAppNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = AppRoutes.Auth.route) {
        authGraph(navController)       // <-- extension function (AuthNavGraph.kt)
        homeNavGraph(navController)    // <-- extension function (HomeNavGraph.kt)
    }
}
```

**What changed:** Previously all 8 `composable()` calls lived in this file. Now it's just 3 lines inside the NavHost -- clean and readable. Each flow is delegated to an extension function in its own file.

---

#### 3. AuthNavGraph.kt -- The Auth Sub-graph

Defined as an **extension function on `NavGraphBuilder`**:

```kotlin
fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(
        startDestination = AppRoutes.LoginScreen.route,
        route = AppRoutes.Auth.route     // <-- graph ID = "auth"
    ) {
        composable(AppRoutes.LoginScreen.route) {
            LoginScreen(onNavigate = {
                navController.navigate(AppRoutes.HomeScreen.route) {
                    popUpTo(AppRoutes.LoginScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable(AppRoutes.SignupScreen.route) { /* -> LoginScreen */ }
        composable(AppRoutes.ResetPinScreen.route) { /* -> LoginScreen */ }
    }
}
```

---

#### 4. HomeNavGraph.kt -- The Main App Sub-graph

```kotlin
fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation(
        startDestination = AppRoutes.HomeScreen.route,
        route = AppRoutes.Main.route     // <-- graph ID = "main"
    ) {
        composable(AppRoutes.HomeScreen.route) { /* multi-destination callback */ }
        composable(AppRoutes.CheckoutScreen.route) { /* -> Confirmation */ }
        composable(AppRoutes.ConfirmationScreen.route) { /* -> Home (with popUpTo) */ }
        composable(AppRoutes.ProfileScreen.route) { /* -> Home (with popUpTo) */ }
        composable(AppRoutes.LogoutScreen.route) { /* -> Login (with popUpTo) */ }
    }
}
```

---

### Extension Functions on NavGraphBuilder -- Explained

```
┌──────────────────────────────────────────────────────────────────────────┐
│              EXTENSION FUNCTION ON NavGraphBuilder                         │
└──────────────────────────────────────────────────────────────────────────┘

  SYNTAX:
  ───────
  fun NavGraphBuilder.authGraph(navController: NavController)
       ^                ^               ^
       |                |               |
       |                |               └── Parameter: so screens can navigate
       |                └── Function name: descriptive, like a module name
       └── Receiver: this function "extends" NavGraphBuilder
           so you can call it inside NavHost { } like a built-in function

  BEFORE vs AFTER:
  ────────────────
  ┌─────────────────────────────────┐     ┌─────────────────────────────────┐
  │  WITHOUT extension functions    │     │  WITH extension functions       │
  │                                 │     │                                 │
  │  NavHost {                      │     │  NavHost {                      │
  │    composable("login") { }      │     │    authGraph(navController)     │
  │    composable("signup") { }     │     │    homeNavGraph(navController)  │
  │    composable("reset") { }      │     │  }                             │
  │    composable("home") { }       │     │                                 │
  │    composable("checkout") { }   │     │  Clean! Each flow in its own   │
  │    composable("confirm") { }    │     │  file.                         │
  │    composable("profile") { }    │     │                                 │
  │    composable("logout") { }     │     │                                 │
  │  }                              │     │                                 │
  │                                 │     │                                 │
  │  One huge file = hard to read   │     │                                 │
  └─────────────────────────────────┘     └─────────────────────────────────┘
```

---

### The `navigation()` Function -- Explained

```kotlin
navigation(
    startDestination = AppRoutes.LoginScreen.route,  // first screen in this group
    route = AppRoutes.Auth.route                     // name of this group
)
```

```
┌──────────────────────────────────────────────────────────────────────────┐
│                      HOW navigation() WORKS                               │
└──────────────────────────────────────────────────────────────────────────┘

  Think of navigation() as creating a "folder" of screens:

  NavHost
  ├── "auth" (navigation graph)                <-- route = "auth"
  |   ├── "login"  (startDestination)          <-- shown first when entering "auth"
  |   ├── "sign_up"
  |   └── "reset_pin"
  |
  └── "main" (navigation graph)                <-- route = "main"
      ├── "Home"  (startDestination)           <-- shown first when entering "main"
      ├── "checkout"
      ├── "confirmation"
      ├── "profile"
      └── "logout"

  When you navigate to "auth" -> it opens "login" (the startDestination)
  When you navigate to "main" -> it opens "Home" (the startDestination)
```

---

### Screen-to-Screen Flow Diagram

```
┌──────────────────────────────────────────────────────────────────────────┐
│                        COMPLETE NAVIGATION FLOW                           │
└──────────────────────────────────────────────────────────────────────────┘

                          AUTH GRAPH ("auth")
               ┌─────────────────────────────────────┐
               │                                     │
               │    ┌─────────┐                      │
               │    │  Login  │ <--- startDestination│
               │    └────┬────┘                      │
               │         │                           │
               │    ┌────┴───────────┐               │
               │    │                │               │
               │    v                v               │
               │ ┌────────┐   ┌───────────┐         │
               │ │ Signup │   │ Reset PIN │         │
               │ └───┬────┘   └─────┬─────┘         │
               │     │              │                │
               │     └──────┬───────┘                │
               │            │ (back to login)        │
               │            v                        │
               └─────────── Login ───────────────────┘
                              │
                              │ LOGIN SUCCESS
                              │ popUpTo("login") { inclusive = true }
                              │ launchSingleTop = true
                              v
                          MAIN GRAPH ("main")
               ┌─────────────────────────────────────┐
               │                                     │
               │    ┌─────────┐                      │
               │    │  Home   │ <--- startDestination│
               │    └────┬────┘                      │
               │         │                           │
               │    ┌────┼────────────┐              │
               │    │    │            │              │
               │    v    v            v              │
               │ ┌────┐┌───────┐ ┌────────┐         │
               │ │Prof││Check- │ │ Logout │         │
               │ │ile ││out    │ │        │         │
               │ └──┬─┘└───┬───┘ └───┬────┘         │
               │    │      │         │               │
               │    │      v         │               │
               │    │ ┌──────────┐   │               │
               │    │ │Confirma- │   │               │
               │    │ │tion      │   │               │
               │    │ └────┬─────┘   │               │
               │    │      │         │               │
               │    └──┬───┘         │               │
               │       │ (back to    │               │
               │       v  Home)      │               │
               │     Home            │               │
               └─────────────────────┼───────────────┘
                                     │
                                     │ LOGOUT
                                     │ popUpTo("Home") { inclusive = true }
                                     v
                                   Login (back to auth)
```

---

### Back Stack Management with Nested Graphs

```
┌──────────────────────────────────────────────────────────────────────────┐
│                    BACK STACK ACROSS FLOWS                                 │
└──────────────────────────────────────────────────────────────────────────┘

  AFTER LOGIN (popUpTo login, inclusive):
  ──────────────────────────────────────
  Before:                After:
  ┌──────────┐           ┌──────────┐
  │  Login   │    -->    │  Home    │ <-- only this
  └──────────┘           └──────────┘
                         Back press -> exits app (Login is gone!)

  AFTER CHECKOUT -> CONFIRMATION -> HOME (popUpTo Home, inclusive):
  ──────────────────────────────────────────────────────────────
  Before:                After:
  ┌──────────────┐       ┌──────────┐
  │ Confirmation │       │  Home    │ <-- clean slate
  ├──────────────┤       └──────────┘
  │ Checkout     │
  ├──────────────┤       Stack is clean! No Checkout or
  │ Home         │       Confirmation left behind.
  └──────────────┘

  AFTER LOGOUT (popUpTo Home, inclusive):
  ──────────────────────────────────────
  Before:                After:
  ┌──────────┐           ┌──────────┐
  │ Logout   │    -->    │  Login   │ <-- fresh start
  ├──────────┤           └──────────┘
  │ Home     │
  └──────────┘           Back press -> exits app (Home is gone!)
```

---

### Key Concept: `launchSingleTop = true`

Prevents **duplicate screens** on the back stack:

```
┌──────────────────────────────────────────────────────────────────────────┐
│                      launchSingleTop EXPLAINED                            │
└──────────────────────────────────────────────────────────────────────────┘

  Without launchSingleTop:          With launchSingleTop = true:
  ─────────────────────────         ──────────────────────────────

  User taps "Home" twice:           User taps "Home" twice:

  ┌──────────┐                      ┌──────────┐
  │  Home    │ <-- duplicate!       │  Home    │ <-- reused, no duplicate
  ├──────────┤                      └──────────┘
  │  Home    │
  └──────────┘
```

---

### Multi-destination Callback Pattern

`HomeScreen` has multiple buttons, each going to a different screen. The callback accepts a **destination** parameter:

```kotlin
// In HomeScreen.kt -- UI only, no NavController
@Composable
fun HomeScreen(onNavigate: (destination: AppRoutes) -> Unit = {}) {
    Button(onClick = { onNavigate(AppRoutes.CheckoutScreen) }) { Text("checkout") }
    Button(onClick = { onNavigate(AppRoutes.ProfileScreen) }) { Text("profile") }
    Button(onClick = { onNavigate(AppRoutes.LogoutScreen) }) { Text("logout") }
}

// In HomeNavGraph.kt -- the NavGraph decides what each destination does
HomeScreen(onNavigate = { destination ->
    when (destination) {
        AppRoutes.CheckoutScreen -> navController.navigate(AppRoutes.CheckoutScreen.route)
        AppRoutes.ProfileScreen  -> navController.navigate(AppRoutes.ProfileScreen.route)
        AppRoutes.LogoutScreen   -> navController.navigate(AppRoutes.LogoutScreen.route)
        else -> Unit
    }
})
```

```
┌──────────────────────────────────────────────────────────────────────────┐
│              MULTI-DESTINATION CALLBACK PATTERN                           │
└──────────────────────────────────────────────────────────────────────────┘

  ┌──────────────────────────────────────────────┐
  │  HomeScreen  (UI only -- no NavController!)  │
  │                                              │
  │  ┌────────────┐  ┌─────────┐  ┌──────────┐  │
  │  │ Checkout   │  │ Profile │  │ Logout   │  │
  │  │ Button     │  │ Button  │  │ Button   │  │
  │  └─────┬──────┘  └────┬────┘  └─────┬────┘  │
  │        │              │             │        │
  └────────┼──────────────┼─────────────┼────────┘
           │              │             │
           v              v             v
     onNavigate(    onNavigate(   onNavigate(
      Checkout)      Profile)      Logout)
           │              │             │
           └──────────────┼─────────────┘
                          │
                          v
               ┌──────────────────────┐
               │  HomeNavGraph.kt     │
               │  when(destination) { │
               │    Checkout -> nav() │
               │    Profile  -> nav() │
               │    Logout   -> nav() │
               │  }                   │
               └──────────────────────┘
```

---

## Type-Safe Routing

### Why Use Sealed Classes for Routes?

```
┌─────────────────────────────────────────────────────────────────────┐
│                   STRING LITERALS vs SEALED CLASS                    │
└─────────────────────────────────────────────────────────────────────┘

  STRING LITERALS (error-prone):
  ─────────────────────────────────
  navController.navigate("profil/John/95")  // Typo: "profil"
                                            // Compiles but crashes at runtime!

  SEALED CLASS (type-safe):
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
        onValueChange = { name = it }  // Updates state -> triggers recomposition
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
  │            v                        v                        │
  │   ┌─────────────────────────────────────────────────────┐   │
  │   │              TextField Components                    │   │
  │   │                                                      │   │
  │   │   value = name          value = score               │   │
  │   │   onValueChange = {}    onValueChange = {}          │   │
  │   └─────────────────────────────────────────────────────┘   │
  │                              │                               │
  │                              v                               │
  │   ┌─────────────────────────────────────────────────────┐   │
  │   │                    Button                            │   │
  │   │   onClick = { onNavigate(name, score) }             │   │
  │   └─────────────────────────────────────────────────────┘   │
  │                              │                               │
  └──────────────────────────────┼───────────────────────────────┘
                                 │
                                 v  Callback to parent (NavGraph)
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
  │  │ TextField: name │──┼──> name = "John"
  │  └─────────────────┘  │
  │  ┌─────────────────┐  │
  │  │ TextField: score│──┼──> score = "95"
  │  └─────────────────┘  │
  │  ┌─────────────────┐  │
  │  │  Button: Click  │──┼──┐
  │  └─────────────────┘  │  │
  └───────────────────────┘  │
                             │
                             v
              ┌──────────────────────────────┐
              │    onNavigate("John", "95")  │
              └──────────────┬───────────────┘
                             │
                             v
              ┌──────────────────────────────┐
              │  Route.Profile.createRoute(  │
              │    "John", "95"              │
              │  ) -> "profile/John/95"      │
              └──────────────┬───────────────┘
                             │
                             v
              ┌──────────────────────────────┐
              │  Route Matching:             │
              │  "profile/{name}/{score}"    │
              │                              │
              │  Extracted:                  │
              │    ARG_NAME  = "John"        │
              │    ARG_SCORE = "95"          │
              └──────────────┬───────────────┘
                             │
                             v
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

---

## Best Practices

### 1. Use Sealed Classes for Type-Safe Routes

```kotlin
sealed class Route(val route: String) {
    data object Home : Route(route = "home")

    data object Profile : Route(route = "profile/{name}/{score}") {
        const val ARG_NAME = "name"
        const val ARG_SCORE = "score"
        fun createRoute(name: String, score: String) = "profile/$name/$score"
    }
}
```

### 2. Use Callback Patterns for Navigation

```kotlin
// GOOD: Decoupled
fun ScreenA(onNavigate: () -> Unit = {}) { }

// AVOID: Tight coupling
fun ScreenA(navController: NavController) { }
```

### 3. Handle Nullable Arguments Safely

```kotlin
ProfileScreen(
    name = backStackEntry.arguments?.getString(Route.Profile.ARG_NAME) ?: "Unknown",
    score = backStackEntry.arguments?.getString(Route.Profile.ARG_SCORE) ?: "0"
)
```

### 4. Use Default Parameter Values for Previews

```kotlin
@Composable
fun HomeScreen(onNavigate: (String, String) -> Unit = { _, _ -> }) { }

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()  // Uses default empty lambda
}
```

### 5. Use popUpTo + launchSingleTop for Flow Transitions

```kotlin
// After login: clear auth stack
navController.navigate(AppRoutes.HomeScreen.route) {
    popUpTo(AppRoutes.LoginScreen.route) { inclusive = true }
    launchSingleTop = true
}
```

### 6. Split Graphs with Extension Functions

```kotlin
// Each flow in its own file
fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(startDestination = "login", route = "auth") { /* ... */ }
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

---

## Quick Reference

### Navigation Setup Checklist

```
┌─────────────────────────────────────────────────────────────────┐
│                    NAVIGATION SETUP CHECKLIST                    │
└─────────────────────────────────────────────────────────────────┘

  Step 1: Define sealed class for routes
  Step 2: Create NavController with rememberNavController()
  Step 3: Setup NavHost with startDestination
  Step 4: Define composable() for each screen
  Step 5: Use callbacks (onNavigate) in screens
  Step 6: Add arguments with navArgument() if needed
  Step 7: Use popUpTo for flow transitions
  Step 8: Split into extension functions when graph grows
```

### Summary Table

| Concept                      | Purpose                         | Example                                                  |
| ---------------------------- | ------------------------------- | -------------------------------------------------------- |
| **Sealed Class Route**       | Type-safe route definitions     | `sealed class Route(val route: String)`                  |
| **NavController**            | Manages navigation state        | `rememberNavController()`                                |
| **NavHost**                  | Hosts navigation graph          | `NavHost(navController, startDestination)`               |
| **composable()**             | Defines screen destination      | `composable(Route.Home.route) { }`                       |
| **navigation()**             | Creates a nested sub-graph      | `navigation(startDest, route) { composable()... }`       |
| **NavGraphBuilder extension**| Splits graph into separate file | `fun NavGraphBuilder.authGraph(nav) { navigation{} }`   |
| **navArgument**              | Defines route parameter         | `navArgument(ARG_NAME) { type = NavType.StringType }`    |
| **navigate()**               | Triggers navigation             | `navController.navigate(Route.Profile.createRoute(...))` |
| **popUpTo**                  | Clears back stack               | `popUpTo(Route.Home.route) { inclusive = true }`         |
| **launchSingleTop**          | Prevents duplicate destinations | `launchSingleTop = true`                                 |
| **Callback Pattern**         | Decouples screens from nav      | `onNavigate: () -> Unit = {}`                            |

---

## Glossary

| Term                           | Definition                                                            |
| ------------------------------ | --------------------------------------------------------------------- |
| **Back Stack**                 | Stack of screens the user has navigated through                       |
| **Composable**                 | A function that defines UI in Jetpack Compose                         |
| **Extension Function**         | A function added to an existing class without modifying it            |
| **NavController**              | Object that manages app navigation                                    |
| **NavGraphBuilder**            | Builder DSL for constructing a navigation graph                       |
| **NavHost**                    | Container that displays current navigation destination                |
| **Nested Graph**               | A group of related screens under one parent route                     |
| **Route**                      | String path that identifies a navigation destination                  |
| **Recomposition**              | Process of re-executing composables when state changes                |
| **Sealed Class**               | A class that restricts its subclasses to a known set                  |
| **State Hoisting**             | Moving state up to a common ancestor composable                       |
| **popUpTo**                    | Navigation option to remove screens from back stack                   |
| **launchSingleTop**            | Prevents creating a new instance if already at the top of stack       |
| **startDestination**           | The first screen shown when a graph or sub-graph is entered           |

---

## References

- [Official Navigation Compose Guide](https://developer.android.com/jetpack/compose/navigation)
- [Navigation Arguments Documentation](https://developer.android.com/guide/navigation/navigation-pass-data)
- [Nested Navigation Graphs](https://developer.android.com/guide/navigation/navigation-nested-graphs)
- [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state)
- [Material 3 Design](https://m3.material.io/)

---

*Last Updated: January 2026*
