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
import androidx.navigation.NavController

@Composable

fun ScreenC (navController: NavController)
{
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        Text (
            text = "Screen C",
            fontSize = 33.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {navController.navigate("A")
            {
                popUpTo ("A")
                {
                    inclusive = true

                }
            }}
        ) {
            Text("Go to screen A")
        }
    }


}
