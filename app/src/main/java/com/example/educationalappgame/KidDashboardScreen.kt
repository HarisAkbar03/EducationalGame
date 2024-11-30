package com.example.educationalapp

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun KidDashboardScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Kid Dashboard")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("level/1") }) {
                Text("Start Level 1")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate("level/2") }) {
                Text("Start Level 2")
            }
        }
    }
}
