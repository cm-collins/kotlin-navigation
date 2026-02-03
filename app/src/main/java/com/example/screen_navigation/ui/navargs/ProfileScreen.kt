package com.example.screen_navigation.ui.navargs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ProfileScreen - Demonstrates receiving navigation arguments.
 *
 * KEY CONCEPTS:
 *
 * 1. RECEIVING NAVIGATION ARGUMENTS:
 *    - Arguments are passed as nullable parameters (String?)
 *    - Nullable because they come from Bundle which may not contain the value
 *    - In NavArgsGraph.kt, arguments are extracted from backStackEntry.arguments
 *
 * 2. WHY NULLABLE?
 *    - Navigation arguments come from the back stack entry's Bundle
 *    - Bundle.getString() returns null if key doesn't exist
 *    - Using nullable types handles this gracefully
 *
 * 3. SAFE ARGUMENT HANDLING:
 *    - Can use Elvis operator for defaults: name ?: "Unknown"
 *    - Can use safe calls: name?.uppercase()
 *    - String templates handle null: "Name: $name" shows "Name: null"
 *
 * HOW ARGUMENTS FLOW:
 * ┌─────────────────────────────────────────────────────────────────────┐
 * │  HomeScreen                                                         │
 * │  onNavigate(name = "John", score = "95")                           │
 * │              │                                                      │
 * │              ▼                                                      │
 * │  NavArgsGraph.kt                                                   │
 * │  navController.navigate("profile/John/95")                         │
 * │              │                                                      │
 * │              ▼                                                      │
 * │  Route matching: "profile/{name}/{score}"                          │
 * │  Extracts: name="John", score="95"                                 │
 * │              │                                                      │
 * │              ▼                                                      │
 * │  ProfileScreen(name = "John", score = "95")                        │
 * └─────────────────────────────────────────────────────────────────────┘
 *
 * @param name The user's name passed from HomeScreen (nullable)
 * @param score The user's score passed from HomeScreen (nullable)
 */
@Composable
fun ProfileScreen(name: String?, score: String?) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen Title
        Text(
            text = "Profile Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ──────────────────────────────────────────────────────────────────
        // DISPLAYING NAVIGATION ARGUMENTS
        // Using string templates to display the received values
        // If null, it will display "Name: null" - consider using Elvis ?: 
        // ──────────────────────────────────────────────────────────────────
        Text(
            text = "Name: ${name ?: "Not provided"}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Score: ${score ?: "Not provided"}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

/**
 * Preview with sample data.
 * Demonstrates how the screen looks with actual values.
 */
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(name = "John Doe", score = "95")
}

/**
 * Preview with null values.
 * Demonstrates how the screen handles missing arguments.
 */
@Preview(showBackground = true, name = "Profile - No Data")
@Composable
fun ProfileScreenEmptyPreview() {
    ProfileScreen(name = null, score = null)
}
