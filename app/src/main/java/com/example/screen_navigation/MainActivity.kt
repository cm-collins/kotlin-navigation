package com.example.screen_navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.screen_navigation.ui.navargs.AppNavArgs
import com.example.screen_navigation.ui.nestednav.navigation.MainAppNav
import com.example.screen_navigation.ui.theme.ScreennavigationTheme
import com.example.screen_navigation.ui.theme.screens.MainNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScreennavigationTheme {

//               AppNavArgs()
                // MainNavGraph()
                MainAppNav()



                }
            }
        }
    }
