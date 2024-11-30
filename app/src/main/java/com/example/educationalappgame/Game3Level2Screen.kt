package com.example.educationalappgame

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun Game3Level2Screen(navController: NavController) {
    val screenWidth = 300.dp // Simulated screen width
    val screenHeight = 600.dp // Simulated screen height
    var playerX by remember { mutableStateOf(0.5f) } // Player position (normalized 0 to 1)
    val meteors = remember { mutableStateListOf<Pair<Float, Float>>() } // List of meteors' (x, y) positions
    var score by remember { mutableStateOf(0) }
    var lives by remember { mutableStateOf(3) } // Player's lives

    // Meteor spawning and movement logic
    LaunchedEffect(Unit) {
        while (lives > 0) {
            meteors.add(Pair(Random.nextFloat(), 0f)) // Add a new meteor at a random x position
            delay(1000) // Spawn meteors every second
        }
    }

    LaunchedEffect(meteors) {
        while (lives > 0) {
            delay(50)
            meteors.replaceAll { Pair(it.first, it.second + 0.02f) } // Move meteors down
            meteors.removeAll { it.second > 1f } // Remove meteors that are off-screen
        }
    }

    // Collision detection
    meteors.forEach { meteor ->
        if (abs(playerX - meteor.first) < 0.1f && meteor.second > 0.9f) {
            lives -= 1 // Player loses a life if hit
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Display meteors
            meteors.forEach { meteor ->
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .offset(
                            x = (meteor.first * screenWidth.value).dp,
                            y = (meteor.second * screenHeight.value).dp
                        )
                        .background(Color.Gray, CircleShape)
                )
            }

            // Player spaceship
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .offset(x = (playerX * screenWidth.value - 30).dp, y = (-30).dp)
                    .background(Color.Blue, CircleShape)
                    .clickable {
                        score++ // Shooting increases the score
                        meteors.removeAll { abs(playerX - it.first) < 0.1f && it.second > 0.8f } // Remove meteors in the shooting range
                    }
            )

            // Player controls
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { if (playerX > 0) playerX -= 0.1f }) {
                    Text("Left")
                }
                Button(onClick = { if (playerX < 1) playerX += 0.1f }) {
                    Text("Right")
                }
            }

            // Score and lives
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Score: $score", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Text(text = "Lives: $lives", style = MaterialTheme.typography.bodyLarge, color = Color.Red)
            }
        }
    }

    // Game Over Dialog
    if (lives <= 0) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Game Over") },
            text = { Text("Your Score: $score") },
            confirmButton = {
                Button(onClick = { navController.navigate("level1_home") }) {
                    Text("Restart")
                }
            }
        )
    }
}
