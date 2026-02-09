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
fun HomeScreen (onNavigate: () -> Unit = {})
{
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Text(
            text = "Home Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button (
            onClick = onNavigate
        ) {
            Text("checkout")
        }

        Button (
            onClick = onNavigate
        ) {
            Text("profile")
        }

        Button (
            onClick = onNavigate
        ) {
            Text("logout")
        }


    }

}

//profile screen

@Composable
fun ProfileScreen (onNavigate: () -> Unit = {})
{
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Text(
            text = "Profile Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button (
            onClick = onNavigate
        ) {
            Text("checkout")
        }

    }

}

//checkout screen
@Composable
fun CheckoutScreen (onNavigate: () -> Unit = {})
{
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Text(
            text = "Checkout Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button (
            onClick = onNavigate
        ) {
            Text("Confirm")
        }

    }

}
//payment confirmation
@Composable
fun PaymentConfirmationScreen (onNavigate: () -> Unit = {})
{
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Text(
            text = "Payment Confirmation Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button (
            onClick = onNavigate
        ) {
            Text("Home")
        }

    }

}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigate = {})

}
//profile screen
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview ()
{
    ProfileScreen()

}
//checkout screen preview
@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview ()
{
    CheckoutScreen()

}
//payment confirmation screen
@Preview(showBackground = true)
@Composable
fun PaymentConfirmationScreenPreview ()
{
    PaymentConfirmationScreen()

}

