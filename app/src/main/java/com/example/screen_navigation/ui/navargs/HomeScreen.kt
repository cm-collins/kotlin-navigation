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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen (onNavigate: (name : String, score : String) -> Unit = {_,_ -> } ) {
    var name by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }

    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home Screen",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = name,
            onValueChange = {name = it},
            label = { Text("Name..") }

        )
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = score,
            onValueChange = {score = it},
            label = { Text("Score...") }

        )

        Spacer(modifier = Modifier.height(30.dp))


        Button(
            onClick = {
                onNavigate(name, score)
            }
        ) {
            Text("Profile sCreen")
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview(modifier: Modifier = Modifier) {
//    HomeScreen()
//}