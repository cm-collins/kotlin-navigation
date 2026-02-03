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
 * ScreenB - Middle screen in the basic navigation flow (A → B → C).
 *
 * KEY CONCEPTS:
 *
 * 1. BACK STACK BEHAVIOR:
 *    - When navigating A → B, Screen A is added to the back stack
 *    - Pressing back from B returns to A
 *    - The system automatically manages this stack
 *
 * 2. CONSISTENT PATTERN:
 *    - All screens follow the same callback pattern
 *    - Makes the codebase consistent and predictable
 *    - Each screen is independent of navigation logic
 *
 * BACK STACK STATE:
 * ┌─────────────────────────────────────────────┐
 * │  When on Screen B:                          │
 * │  ┌──────────────────┐                       │
 * │  │    Screen B      │ ◄── Current (top)     │
 * │  ├──────────────────┤                       │
 * │  │    Screen A      │ ◄── Can go back to    │
 * │  └──────────────────┘                       │
 * └─────────────────────────────────────────────┘
 *
 * @param onNavigate Callback triggered when user clicks navigation button.
 *                   In MainNavGraph, this navigates to Screen C.
 */
@Composable
fun ScreenB(onNavigate: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen identifier
        Text(
            text = "Screen B",
            fontSize = 33.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Navigation button with callback pattern
        Button(onClick = { onNavigate() }) {
            Text("Go to Screen C")
        }
    }
}

/**
 * Preview for ScreenB.
 */
@Preview(showBackground = true)
@Composable
fun ScreenBPreview() {
    ScreenB()
}
