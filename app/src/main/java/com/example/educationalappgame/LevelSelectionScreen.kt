package com.example.educationalappgame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LevelSelectionScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Select a Level", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("level1_game1") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Level 1")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate("level2_game1") }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Level 2")
            }
        }
    }
}
