package com.example.screen_navigation.ui.navargs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * HomeScreen - Demonstrates state management and navigation with arguments.
 *
 * KEY CONCEPTS:
 *
 * 1. STATE MANAGEMENT WITH remember + mutableStateOf:
 *    - `remember` preserves state across recompositions
 *    - `mutableStateOf` creates observable state that triggers recomposition when changed
 *    - `by` delegate allows direct property access (name instead of name.value)
 *
 * 2. CALLBACK PATTERN FOR NAVIGATION:
 *    - `onNavigate: (name: String, score: String) -> Unit` is a lambda callback
 *    - Screen doesn't know about NavController (decoupled)
 *    - Navigation logic lives in the NavGraph, not the screen
 *    - Default value `= { _, _ -> }` allows preview without navigation
 *
 * 3. CONTROLLED TEXT FIELDS:
 *    - `value = name` - TextField displays the current state
 *    - `onValueChange = { name = it }` - Updates state when user types
 *    - This is the "single source of truth" pattern
 *
 * @param onNavigate Callback invoked when user wants to navigate to ProfileScreen.
 *                   Passes the entered name and score as arguments.
 */
@Composable
fun HomeScreen(onNavigate: (name: String, score: String) -> Unit = { _, _ -> }) {

    // ══════════════════════════════════════════════════════════════════════
    // STATE DECLARATION
    // ══════════════════════════════════════════════════════════════════════
    // Using 'by' delegate with remember + mutableStateOf for reactive state
    // When these values change, the Composable automatically recomposes
    //
    // Best practice:
    // - prefer rememberSaveable for simple UI state that should survive
    //   configuration changes (like rotation) and process recreation.
    var name by rememberSaveable { mutableStateOf("") }
    var score by rememberSaveable { mutableStateOf("") }

    // ══════════════════════════════════════════════════════════════════════
    // UI LAYOUT
    // ══════════════════════════════════════════════════════════════════════
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen Title
        Text(
            text = "Home Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // ──────────────────────────────────────────────────────────────────
        // CONTROLLED TEXT FIELD - Name Input
        // The TextField is "controlled" because its value comes from state
        // ──────────────────────────────────────────────────────────────────
        TextField(
            value = name,                           // Display current state
            onValueChange = { name = it },          // Update state on change
            label = { Text("Enter Name") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ──────────────────────────────────────────────────────────────────
        // CONTROLLED TEXT FIELD - Score Input
        // ──────────────────────────────────────────────────────────────────
        TextField(
            value = score,
            onValueChange = { score = it },
            label = { Text("Enter Score") }
        )

        Spacer(modifier = Modifier.height(30.dp))

        // ──────────────────────────────────────────────────────────────────
        // NAVIGATION BUTTON
        // Invokes the callback with current state values
        // The actual navigation happens in NavArgsGraph.kt
        // ──────────────────────────────────────────────────────────────────
        Button(
            onClick = { onNavigate(name, score) }
        ) {
            Text("Go to Profile Screen")
        }
    }
}

/**
 * Preview function for HomeScreen.
 * Uses default empty lambda for onNavigate since we can't navigate in preview.
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}