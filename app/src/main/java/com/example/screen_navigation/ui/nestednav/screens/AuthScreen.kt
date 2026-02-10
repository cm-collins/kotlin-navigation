package com.example.screen_navigation.ui.nestednav.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.screen_navigation.ui.nestednav.navigation.AppRoutes

@Composable
fun LoginScreen(onNavigate: (route: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Text(
            text = "Login Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onNavigate(AppRoutes.LoginScreen.route) }
        ) {
            Text("login")
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onNavigate(AppRoutes.ResetPinScreen.route) }
        ) {
            Text("reset pin ")
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onNavigate(AppRoutes.SignupScreen.route) }
        ) {
            Text("signup")
        }
    }

}

//Signup Screen
@Composable
fun SignupScreen(onNavigate: (route: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Text(
            text = "Sign up Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onNavigate(AppRoutes.SignupScreen.route) }
        ) {
            Text("Sign Up")
        }
    }

}

//reset pinscreen
@Composable
fun ResetPinScreen(onNavigate: (route: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Text(
            text = "Reset Pin Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onNavigate(AppRoutes.ResetPinScreen.route) }
        ) {
            Text("Reset Pin")
        }
    }

}


