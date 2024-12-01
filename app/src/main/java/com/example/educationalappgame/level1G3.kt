package com.example.educationalappgame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun Game3Level1Screen(navController: NavController) {
    val screenWidth = 300f // Simulated screen width in pixels
    val screenHeight = 600f // Simulated screen height in pixels
    var playerX by remember { mutableStateOf(0.5f) } // Player position (normalized 0 to 1)
    val meteors = remember { mutableStateListOf<Pair<Float, Float>>() } // List of meteors' (x, y) positions
    var score by remember { mutableStateOf(0) }
    var lives by remember { mutableStateOf(3) } // Player's lives
    var gameOver by remember { mutableStateOf(false) } // Game Over state

    // Meteor spawning logic
    LaunchedEffect(Unit) {
        while (!gameOver) {
            meteors.add(Pair(Random.nextFloat(), 0f)) // Add a new meteor at a random x position (top of screen)
            delay(1500) // Spawn meteors every 1.5 seconds
        }
    }

    // Meteor movement logic
    LaunchedEffect(meteors) {
        while (!gameOver) {
            delay(50) // Control meteor falling speed
            meteors.replaceAll { Pair(it.first, it.second + 0.02f) } // Move meteors down gradually
            meteors.removeAll { it.second > 1f } // Remove meteors that are off-screen
        }
    }

    // Collision detection and score increment
    meteors.forEach { meteor ->
        if (abs(playerX - meteor.first) < 0.1f && meteor.second > 0.9f) {
            lives -= 1 // Player loses a life if hit
            meteors.remove(meteor) // Remove the meteor after collision
            if (lives <= 0) gameOver = true
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.background), // Replace with your background image resource
                contentDescription = "Background",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Meteors
            meteors.forEach { meteor ->
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .offset(
                            x = (meteor.first * screenWidth).dp,
                            y = (meteor.second * screenHeight).dp
                        )
                        .background(Color.Gray, RectangleShape)
                )
            }

            // Player (Flat Line)
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(10.dp) // Make it a flat line
                    .offset(
                        x = (playerX * screenWidth - 50).dp, // Center the player horizontally
                        y = (screenHeight - 40).dp // Position the player near the bottom
                    )
                    .background(Color.Blue, RectangleShape)
            )

            // Player controlsS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
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
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Score: $score", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Text(text = "Lives: $lives", style = MaterialTheme.typography.bodyLarge, color = Color.Red)
            }
        }
    }

    // Game Over Dialog
    if (gameOver) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Game Over") },
            text = { Text("Your Score: $score") },
            confirmButton = {
                Button(onClick = {
                    // Restart the game
                    playerX = 0.5f
                    meteors.clear()
                    score = 0
                    lives = 3
                    gameOver = false
                }) {
                    Text("Restart")
                }
            },
            dismissButton = {
                Button(onClick = { navController.navigate("level1_home") }) {
                    Text("Exit")
                }
            }
        )
    }
}
