# Jetpack Compose Navigation with Arguments

> A comprehensive guide to implementing navigation and passing data between screens in Jetpack Compose.

---

## Table of Contents

1. [Overview](#overview)
2. [Architecture Diagram](#architecture-diagram)
3. [Core Concepts](#core-concepts)
4. [Implementation Guide](#implementation-guide)
5. [Code Breakdown](#code-breakdown)
6. [Navigation Flow](#navigation-flow)
7. [Best Practices](#best-practices)
8. [Common Patterns](#common-patterns)

---

## Overview

This project demonstrates **Jetpack Compose Navigation** with **Navigation Arguments** - a pattern for navigating between screens while passing data. The implementation uses the `navigation-compose` library to create a type-safe, declarative navigation system.

### Key Features Implemented

- ✅ NavController for navigation management
- ✅ NavHost for hosting navigation graph
- ✅ Route-based navigation with arguments
- ✅ Type-safe argument passing between screens
- ✅ State management with `remember` and `mutableStateOf`

---

## Architecture Diagram

### High-Level Navigation Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                           MainActivity                               │
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │                    ScreennavigationTheme                       │  │
│  │  ┌─────────────────────────────────────────────────────────┐  │  │
│  │  │                      AppNavArgs()                        │  │  │
│  │  │  ┌────────────────────────────────────────────────────┐ │  │  │
│  │  │  │                   NavController                     │ │  │  │
│  │  │  │         (manages navigation state)                  │ │  │  │
│  │  │  └────────────────────────────────────────────────────┘ │  │  │
│  │  │                          │                               │  │  │
│  │  │                          ▼                               │  │  │
│  │  │  ┌────────────────────────────────────────────────────┐ │  │  │
│  │  │  │                     NavHost                         │ │  │  │
│  │  │  │            (hosts composable screens)               │ │  │  │
│  │  │  │  ┌─────────────────┐    ┌─────────────────────┐    │ │  │  │
│  │  │  │  │   HomeScreen    │───►│   ProfileScreen     │    │ │  │  │
│  │  │  │  │  route: "Home"  │    │ route: "profile/    │    │ │  │  │
│  │  │  │  │                 │    │   {name}/{score}"   │    │ │  │  │
│  │  │  │  └─────────────────┘    └─────────────────────┘    │ │  │  │
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
    startDestination = "Home"
) {
    // Screen definitions go here
}
```

---

### 3. Routes

Routes are **string-based paths** that identify destinations. They can include:

- **Static routes:** `"Home"`, `"Settings"`
- **Dynamic routes with arguments:** `"profile/{name}/{score}"`

```
Route Pattern Examples:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Static Route     │  "Home"
  ─────────────────┼─────────────────────────────
  With 1 Argument  │  "profile/{userId}"
  ─────────────────┼─────────────────────────────
  With 2 Arguments │  "profile/{name}/{score}"
  ─────────────────┼─────────────────────────────
  Optional Args    │  "search?query={query}"
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

### 4. Navigation Arguments

Arguments allow **passing data** between screens via the route.

#### Defining Arguments

```kotlin
composable(
    route = "profile/{name}/{score}",
    arguments = listOf(
        navArgument("name") { type = NavType.StringType },
        navArgument("score") { type = NavType.StringType }
    )
) { backStackEntry ->
    // Access arguments
    val name = backStackEntry.arguments?.getString("name")
    val score = backStackEntry.arguments?.getString("score")
}
```

#### Supported NavType Values

| NavType              | Kotlin Type | Example      |
| -------------------- | ----------- | ------------ |
| `NavType.StringType` | String      | `"John"`     |
| `NavType.IntType`    | Int         | `42`         |
| `NavType.LongType`   | Long        | `123456789L` |
| `NavType.FloatType`  | Float       | `3.14f`      |
| `NavType.BoolType`   | Boolean     | `true`       |

---

## Implementation Guide

### Step-by-Step Setup

```
┌─────────────────────────────────────────────────────────────────┐
│                    IMPLEMENTATION STEPS                          │
└─────────────────────────────────────────────────────────────────┘

  STEP 1                    STEP 2                    STEP 3
  ──────────────────────    ──────────────────────    ──────────────────────
  Create NavController      Setup NavHost             Define Composable
                                                      Destinations
       │                         │                         │
       ▼                         ▼                         ▼
  ┌──────────────┐         ┌──────────────┐         ┌──────────────┐
  │ val nav =    │         │ NavHost(     │         │ composable(  │
  │ rememberNav  │    ───► │   navController,│  ───► │   route,     │
  │ Controller() │         │   startDest  │         │   arguments  │
  │              │         │ )            │         │ ) { }        │
  └──────────────┘         └──────────────┘         └──────────────┘
```

---

## Code Breakdown

### File Structure

```
app/src/main/java/com/example/screen_navigation/
│
├── MainActivity.kt              # Entry point, sets up theme
│
└── ui/
    └── navargs/
        ├── NavArgsGraph.kt      # Navigation graph definition
        ├── HomeScreen.kt        # Source screen with input
        └── ProfileScreen.kt     # Destination screen with data display
```

---

### MainActivity.kt

The entry point that sets up the app theme and launches the navigation graph.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScreennavigationTheme {
                AppNavArgs()  // Launches the navigation graph
            }
        }
    }
}
```

**Key Points:**

- `enableEdgeToEdge()` - Enables edge-to-edge display
- `setContent {}` - Defines the composable UI
- `ScreennavigationTheme` - Applies Material 3 theming
- `AppNavArgs()` - The root navigation composable

---

### NavArgsGraph.kt (AppNavArgs)

The navigation graph that defines all screens and their routes.

```kotlin
@Composable
fun AppNavArgs(modifier: Modifier = Modifier) {
    // STEP 1: Create NavController
    val navController = rememberNavController()

    // STEP 2: Define NavHost with starting destination
    NavHost(navController = navController, startDestination = "Home") {

        // STEP 3: Define HomeScreen destination
        composable("Home") {
            HomeScreen(onNavigate = { name, score ->
                // Navigate with arguments embedded in route
                navController.navigate("profile/$name/$score")
            })
        }

        // STEP 4: Define ProfileScreen with arguments
        composable(
            route = "profile/{name}/{score}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("score") { type = NavType.StringType }
            )
        ) {
            ProfileScreen(
                name = it.arguments?.getString("name"),
                score = it.arguments?.getString("score")
            )
        }
    }
}
```

---

### HomeScreen.kt

The source screen with user input and navigation trigger.

```kotlin
@Composable
fun HomeScreen(onNavigate: (name: String, score: String) -> Unit = { _, _ -> }) {
    // State management using remember + mutableStateOf
    var name by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen", fontSize = 30.sp)

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name..") }
        )

        TextField(
            value = score,
            onValueChange = { score = it },
            label = { Text("Score...") }
        )

        Button(onClick = { onNavigate(name, score) }) {
            Text("Profile Screen")
        }
    }
}
```

**State Management Pattern:**

```
┌────────────────────────────────────────────────────────────────┐
│                    STATE HOISTING PATTERN                       │
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
                                 ▼  Callback to parent (NavArgsGraph)
                     ┌───────────────────────┐
                     │ navController.navigate│
                     │ ("profile/$name/$score")│
                     └───────────────────────┘
```

---

### ProfileScreen.kt

The destination screen that receives and displays the arguments.

```kotlin
@Composable
fun ProfileScreen(name: String?, score: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile Screen", fontSize = 30.sp)

        Text(text = "Name: $name", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = "Score: $score", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}
```

**Note:** Arguments are nullable (`String?`) because they come from the navigation back stack entry.

---

## Navigation Flow

### Complete Data Flow Diagram

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
              │  navController.navigate(     │
              │    "profile/John/95"         │
              │  )                           │
              └──────────────┬───────────────┘
                             │
                             ▼
              ┌──────────────────────────────┐
              │  Route Matching:             │
              │  "profile/{name}/{score}"    │
              │                              │
              │  Extracted:                  │
              │    name  = "John"            │
              │    score = "95"              │
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

### 1. Use Callback Patterns for Navigation

```kotlin
// ✅ Good: Pass navigation as callback
@Composable
fun HomeScreen(onNavigate: (String, String) -> Unit) {
    Button(onClick = { onNavigate(name, score) }) { }
}

// ❌ Avoid: Passing NavController directly to screens
@Composable
fun HomeScreen(navController: NavController) {
    // Tight coupling to navigation
}
```

### 2. Define Routes as Constants

```kotlin
// ✅ Recommended: Centralized route definitions
object Routes {
    const val HOME = "home"
    const val PROFILE = "profile/{name}/{score}"

    fun profileWithArgs(name: String, score: String) = "profile/$name/$score"
}
```

### 3. Handle Nullable Arguments Safely

```kotlin
// ✅ Safe handling with Elvis operator
ProfileScreen(
    name = it.arguments?.getString("name") ?: "Unknown",
    score = it.arguments?.getString("score") ?: "0"
)
```

### 4. Use Default Parameter Values

```kotlin
// ✅ Allows preview and testing without navigation
fun HomeScreen(onNavigate: (String, String) -> Unit = { _, _ -> })
```

---

## Common Patterns

### Optional Arguments

```kotlin
composable(
    route = "search?query={query}",
    arguments = listOf(
        navArgument("query") {
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
    route = "profile/{userId}",
    deepLinks = listOf(
        navDeepLink { uriPattern = "https://example.com/profile/{userId}" }
    )
) { }
```

### Nested Navigation

```kotlin
NavHost(navController, startDestination = "main") {
    navigation(startDestination = "home", route = "main") {
        composable("home") { HomeScreen() }
        composable("settings") { SettingsScreen() }
    }
    navigation(startDestination = "login", route = "auth") {
        composable("login") { LoginScreen() }
        composable("register") { RegisterScreen() }
    }
}
```

---

## Summary

| Concept           | Purpose                    | Key Function                               |
| ----------------- | -------------------------- | ------------------------------------------ |
| **NavController** | Manages navigation state   | `rememberNavController()`                  |
| **NavHost**       | Hosts navigation graph     | `NavHost(navController, startDestination)` |
| **composable()**  | Defines screen destination | `composable(route, arguments)`             |
| **navArgument**   | Defines route parameter    | `navArgument(name) { type = NavType.X }`   |
| **navigate()**    | Triggers navigation        | `navController.navigate("route/arg")`      |

---

## References

- [Official Navigation Compose Guide](https://developer.android.com/jetpack/compose/navigation)
- [Navigation Arguments Documentation](https://developer.android.com/guide/navigation/navigation-pass-data)
- [Material 3 Design](https://m3.material.io/)

---

_Last Updated: January 2026_
