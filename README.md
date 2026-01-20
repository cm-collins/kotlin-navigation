## kotlin-navigation

This repo is a small **Jetpack Compose** project to learn **screen navigation in Android** using **Navigation Compose** (not Flutter).

### What this project builds

- **3 simple screens**: `ScreenA`, `ScreenB`, `ScreenC`
- **Single-Activity Compose app**: `MainActivity` hosts Compose via `setContent { ... }`
- **Navigation graph**: `MainNavGraph()` defines destinations and the start route
- **Flow**:
  - A → B → C using `navController.navigate("B")`, `navigate("C")`
  - C → A with **back stack control** using `popUpTo("A") { inclusive = true }`

### Where the code lives (quick map)

- `app/src/main/java/com/example/screen_navigation/MainActivity.kt`
  - App entry point. Calls `setContent { ... }` and shows `MainNavGraph()`.
- `app/src/main/java/com/example/screen_navigation/ui/screens/NavGraph.kt`
  - The navigation host: creates the `NavController`, defines destinations and the start route.
- `app/src/main/java/com/example/screen_navigation/ui/screens/ScreenA.kt`
- `app/src/main/java/com/example/screen_navigation/ui/screens/ScreenB.kt`
- `app/src/main/java/com/example/screen_navigation/ui/screens/ScreenC.kt`
  - Simple UI screens with buttons that call `navController.navigate(...)`.

### Compose concepts covered (in this repo)

- **Composable functions (`@Composable`)**
  - UI is built as functions: `ScreenA(...)`, `ScreenB(...)`, `ScreenC(...)`, `MainNavGraph()`
- **Declarative UI mindset**
  - You describe *what the UI should look like* for a given state, and Compose updates the screen when state changes.
  - In this repo the screens are static, but the same pattern scales to dynamic UIs.
- **Composition + recomposition (core Compose concept)**
  - Composition is when Compose builds the UI tree from your composables.
  - Recomposition happens when state read by a composable changes, and Compose re-runs the relevant composables.
  - Tip: put stable objects (like `NavController`) behind `remember...` so they’re not recreated on recomposition.
- **Layouts**
  - `Column` for vertical layout
  - `Spacer` for spacing
- **Modifiers**
  - `Modifier.fillMaxSize()` for sizing
- **Material 3 UI components**
  - `Text` and `Button`
  - Typical Compose pattern: UI + `onClick { ... }` events

### Navigation Compose concepts covered

- **NavController**
  - Created once per graph using `rememberNavController()`
  - Passed to screens so they can trigger navigation
- **NavHost**
  - Defines the navigation container and `startDestination`
- **Destinations**
  - Defined via `composable(route) { ScreenX(navController) }`
  - Routes are currently simple string IDs: `"A"`, `"B"`, `"C"`
- **Back stack behavior**
  - `navigate("B")` / `navigate("C")` pushes a new destination on the back stack
  - `popUpTo("A") { inclusive = true }` clears up to A and removes A too, then navigation re-adds A
  - Why this matters: it’s how you prevent the user from “backing into” intermediate screens after completing a flow

### Understanding `popUpTo(...){ inclusive = true }` (what it’s doing here)

In `ScreenC`, navigation goes back to `"A"` like this:

- **Navigate to "A"**
- **Pop up to "A" (inclusive)**
  - “Pop up to A” means: remove everything above route `"A"` from the back stack.
  - `inclusive = true` additionally removes `"A"` itself.
  - Result: you get a “fresh” A at the top, instead of returning to an older instance in the stack.

If you wanted different behavior:

- **Keep the existing A instance**: `popUpTo("A") { inclusive = false }`
- **Avoid duplicates when navigating to a destination that might already be on top**: `launchSingleTop = true` (not used in this repo yet)

### Takeaway notes

- **Keep a single source of truth for navigation**: build your routes and destinations inside a `NavHost` function (here: `MainNavGraph()`).
- **Create the NavController once**: `rememberNavController()` should live close to the `NavHost`, not inside each screen.
- **Screens should be simple UI**: they receive what they need (`NavController` here) and emit events (button clicks) that call `navigate(...)`.
- **Back stack is part of UX**: use `popUpTo(...)` when you want to control what “Back” means after certain flows.
- **Hard-coded string routes work for learning**: but as apps grow, prefer centralizing routes (see “Next things to try”).

### Notes on the dependency

This project uses **Navigation Compose** (`androidx.navigation:navigation-compose`). The Gradle alias used in `app/build.gradle.kts` is:

- `implementation(libs.androidx.navigation.compose)`

Which maps to this version-catalog entry in `gradle/libs.versions.toml`:

- `androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", ... }`

### How to run

- Open the project in Android Studio.
- Click **Sync Now** when prompted (or run **File → Sync Project with Gradle Files**).
- Run the **app** configuration on an emulator or a device.

### Common setup issue (the one you hit)

If Android Studio shows:

- `Unresolved reference: navigation` for `implementation(libs.androidx.navigation.compose)`

It usually means the version-catalog alias is missing or misnamed. This repo expects a catalog entry named:

- `androidx-navigation-compose` (which becomes `libs.androidx.navigation.compose` in Gradle Kotlin DSL)

### Next things to try (optional, not implemented yet)

- **Centralize routes** with a sealed class or enum (instead of raw `"A"`, `"B"`, `"C"` strings).
- Add a **parameterized route** (e.g., `"details/{id}"`) and pass arguments between screens.
- Experiment with **back handling** and navigation options like `launchSingleTop` and `restoreState`.
