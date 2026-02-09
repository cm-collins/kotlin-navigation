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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen (onNavigate: () -> Unit = {})
{
    Column (
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

        Button (
            onClick = onNavigate
        ) {
            Text("Login")
        }
    }

}

//Signup Screen
@Composable
fun SignupScreen (onNavigate: () -> Unit = {})
{
    Column (
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

        Button (
            onClick = onNavigate
        ) {
            Text("Sign Up")
        }
    }

}

//reset pinscreen
@Composable
fun ResetPinScreen (onNavigate: () -> Unit = {})
{
    Column (
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

        Button (
            onClick = onNavigate
        ) {
            Text("Reset Pin")
        }
    }

}



//login screen previews
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
//Signup Screen preview

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview ()
{
    SignupScreen()

}

//reset pin screen preview
@Preview(showBackground = true)
@Composable
fun ResetPinScreenPreview ()
{
    ResetPinScreen()

}