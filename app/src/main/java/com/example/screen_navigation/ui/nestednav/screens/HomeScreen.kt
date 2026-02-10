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
import com.example.screen_navigation.ui.nestednav.navigation.AppRoutes

@Composable
fun HomeScreen(onNavigate: (destination: AppRoutes) -> Unit = {}) {
    Column(
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

        Button(
            onClick = { onNavigate(AppRoutes.CheckoutScreen) }
        ) {
            Text("checkout")
        }

        Button(
            onClick = { onNavigate(AppRoutes.ProfileScreen) }
        ) {
            Text("profile")
        }

        Button(
            onClick = { onNavigate(AppRoutes.LogoutScreen) }
        ) {
            Text("logout")
        }


    }

}

@Preview(showBackground = true)
@Composable
fun HomeNestedScreenPreview() {
    HomeScreen()
}

//profile screen

@Composable
fun ProfileScreen(onNavigate: () -> Unit = {}) {
    Column(
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

        Button(
            onClick = onNavigate
        ) {
            Text("checkout")
        }

    }

}

//checkout screen
@Composable
fun CheckoutScreen(onNavigate: () -> Unit = {}) {
    Column(
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

        Button(
            onClick = onNavigate
        ) {
            Text("Confirm")
        }

    }

}

//payment confirmation
@Composable
fun ConfirmationScreen(onNavigate: () -> Unit = {}) {
    Column(
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

        Button(
            onClick = onNavigate
        ) {
            Text("Home")
        }

    }


}

//logout screen
@Composable
fun LogoutScreen(onNavigate: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Logout Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onNavigate
        ) {
            Text("Logout")
        }
    }
}



