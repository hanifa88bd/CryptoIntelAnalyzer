package com.cryptointel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cryptointel.ui.AboutScreen
import com.cryptointel.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                Scaffold(
                    topBar = {
                        SmallTopAppBar(title = { Text("Crypto Intel Analyzer") })
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = nav.currentDestination?.route == "home",
                                onClick = { nav.navigate("home") },
                                label = { Text("Home") },
                                icon = {}
                            )
                            NavigationBarItem(
                                selected = nav.currentDestination?.route == "about",
                                onClick = { nav.navigate("about") },
                                label = { Text("About") },
                                icon = {}
                            )
                        }
                    }
                ) { pad ->
                    NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(pad)) {
                        composable("home") { HomeScreen() }
                        composable("about") { AboutScreen() }
                    }
                }
            }
        }
    }
}