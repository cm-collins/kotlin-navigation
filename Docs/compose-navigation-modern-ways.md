# Modern / “New Ways” of Jetpack Compose Navigation (2026 notes)

This file complements `Docs/jetpack-compose-navigation.md` by explaining **more modern and simpler patterns** you’ll see in newer Compose codebases.

---

## What we’re doing today (already good)

Your current code uses:

- **Callback navigation**: screens take `onNavigate` lambdas, not `NavController`
- **Centralized routes**: sealed classes (`Route`, `NavArgsRoute`)
- **Arguments with `navArgument`** and extraction from `backStackEntry.arguments`

These are solid best practices and widely used.

---

## Small improvements we applied (best-practice upgrades)

### 1) Encode / Decode route arguments

When you build routes like:

```kotlin
navController.navigate("profile/$name/$score")
```

you can break navigation when:

- `name` contains spaces (`"John Doe"`)
- `name` contains `/`, `?`, `&`, `#`, etc.

**Modern safe approach**:

- `Uri.encode(value)` when navigating
- `Uri.decode(value)` when reading args

This is now implemented in `NavArgsRoute.Profile.createRoute()` and in arg extraction.

---

### 2) Use `rememberSaveable` for text input

For form-like UI state (TextFields), prefer:

```kotlin
var name by rememberSaveable { mutableStateOf("") }
```

This keeps user input during:

- screen rotation
- process recreation (when possible)

This is now implemented in `HomeScreen.kt`.

---

## The “modern type-safe navigation” approach (less stringy)

Even with sealed-class routes, you still ultimately pass strings to `navigate(...)`.

Newer Navigation Compose versions introduce **typed destinations** (type-safe routes + args), often implemented with **Kotlin serialization**.

### Why this is better

- No manual `navArgument("name") { type = ... }`
- No manual string key lookups (`getString("name")`)
- You navigate with **real types**, not route strings

### What it looks like (conceptual example)

> This is a learning example—API details vary by navigation-compose version.

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data class Profile(val name: String, val score: Int)

NavHost(navController, startDestination = Home) {
    composable<Home> {
        HomeScreen { name, score ->
            navController.navigate(Profile(name = name, score = score.toInt()))
        }
    }
    composable<Profile> { entry ->
        val args = entry.toRoute<Profile>()
        ProfileScreen(name = args.name, score = args.score.toString())
    }
}
```

### What you’d need to adopt this

- Kotlin serialization plugin enabled
- `kotlinx-serialization-json` dependency
- A navigation-compose version that supports typed destinations

This approach is **cleaner**, but your sealed-class routing is still perfectly acceptable and more beginner-friendly.

---

## Another modern alternative: Query parameters for optional args

Path segments (like `profile/{name}/{score}`) are great, but query params are often simpler for **optional** values:

```
profile?name=John&score=95
```

Benefits:

- Optional args are natural
- Less risk with slashes in values (still encode!)

Tradeoff:

- Slightly more parsing / defaults

---

## Recommended “simple + best practice” checklist for your current style

- **Routes**: sealed classes with constants + helper builders ✅
- **No NavController in screens**: callbacks ✅
- **Encode args**: `Uri.encode/decode` ✅
- **Form state**: `rememberSaveable` ✅
- **Numbers**: prefer passing `Int`/`Long` rather than `String` when possible
- **File organization**:
  - `routes/` (sealed classes)
  - `navgraphs/` (NavHost graphs)
  - `screens/` (UI composables only)

---

## Next learning step (if you want)

If you want the “latest/simple” style, we can migrate `navargs/` to **typed destinations** (serialization-based), so you don’t manually define argument keys/types at all.
