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
 * ScreenC - Final screen demonstrating popUpTo navigation behavior.
 *
 * KEY CONCEPTS:
 *
 * 1. popUpTo NAVIGATION:
 *    - When navigating from C back to A, we use popUpTo
 *    - This clears intermediate screens from the back stack
 *    - Prevents the user from having a long back stack (A→B→C→A→B→C...)
 *
 * 2. inclusive = true:
 *    - When used with popUpTo, removes the target destination too
 *    - Creates a "fresh start" at Screen A
 *    - Without inclusive, you'd have two Screen A's in the stack
 *
 * BACK STACK BEHAVIOR:
 * ┌─────────────────────────────────────────────────────────────────────┐
 * │                                                                     │
 * │  Before navigation (on Screen C):    After popUpTo with inclusive: │
 * │  ┌──────────────────┐                ┌──────────────────┐          │
 * │  │    Screen C      │ ◄── Current    │    Screen A      │ ◄── New  │
 * │  ├──────────────────┤                └──────────────────┘          │
 * │  │    Screen B      │                                              │
 * │  ├──────────────────┤                Fresh start! Only Screen A    │
 * │  │    Screen A      │                in the back stack.            │
 * │  └──────────────────┘                                              │
 * │                                                                     │
 * └─────────────────────────────────────────────────────────────────────┘
 *
 * WITHOUT popUpTo:
 * - Stack would grow: A → B → C → A → B → C → A...
 * - Back button would go through all screens
 *
 * WITH popUpTo + inclusive:
 * - Stack stays clean: just A
 * - Back button exits the app (expected behavior)
 *
 * @param onNavigate Callback for navigation. In MainNavGraph, this navigates
 *                   to Screen A with popUpTo to clear the back stack.
 */
@Composable
fun ScreenC(onNavigate: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen identifier
        Text(
            text = "Screen C",
            fontSize = 33.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Navigation button - triggers popUpTo navigation in NavGraph
        Button(onClick = { onNavigate() }) {
            Text("Go to Screen A")
        }
    }
}

/**
 * Preview for ScreenC.
 */
@Preview(showBackground = true)
@Composable
fun ScreenCPreview() {
    ScreenC()
}
