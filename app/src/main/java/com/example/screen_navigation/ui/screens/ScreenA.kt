package com.example.screen_navigation.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ScreenA - First screen in the basic navigation flow (A → B → C → A).
 *
 * KEY CONCEPTS:
 *
 * 1. CALLBACK PATTERN (onNavigate):
 *    - Instead of passing NavController directly, we use a callback
 *    - This decouples the screen from navigation implementation
 *    - Makes the composable reusable and testable
 *
 * 2. DEFAULT PARAMETER VALUE:
 *    - `onNavigate: () -> Unit = {}` provides an empty default
 *    - Allows the composable to be previewed without navigation
 *    - Makes the parameter optional when calling
 *
 * 3. UNIT RETURN TYPE:
 *    - `() -> Unit` means "a function that takes nothing and returns nothing"
 *    - Perfect for simple navigation without passing data
 *
 * NAVIGATION FLOW:
 * ┌──────────┐      ┌──────────┐      ┌──────────┐
 * │ Screen A │ ───► │ Screen B │ ───► │ Screen C │
 * └──────────┘      └──────────┘      └──────────┘
 *      ▲                                   │
 *      └───────────────────────────────────┘
 *              (with popUpTo for clean back stack)
 *
 * @param onNavigate Callback triggered when user clicks the navigation button.
 *                   In MainNavGraph, this navigates to Screen B.
 */
@Composable
fun ScreenA(onNavigate: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen identifier
        Text(
            text = "Screen A",
            fontSize = 33.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // ──────────────────────────────────────────────────────────────────
        // NAVIGATION BUTTON
        // onClick invokes the callback - actual navigation logic is in NavGraph
        // ──────────────────────────────────────────────────────────────────
        Button(onClick = { onNavigate() }) {
            Text("Go to Screen B")
        }
    }
}

/**
 * Preview for ScreenA.
 * Default empty lambda allows preview without navigation setup.
 */
@Preview(showBackground = true)
@Composable
fun ScreenAPreview() {
    ScreenA()
}
