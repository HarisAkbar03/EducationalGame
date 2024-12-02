package com.example.educationalappgame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Game1Level2Screen(navController: NavController) {
    val gridSize = 5 // 5x5 Grid
    val playerStart = Pair(0, 0)
    val goalPosition = Pair(4, 4)
    var playerPosition by remember { mutableStateOf(playerStart) }
    var showWinDialog by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.screen1), // Replace with your image resource
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Grid and Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(gridSize) { row ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(gridSize) { col ->
                                val isPlayer = playerPosition == Pair(row, col)
                                val isGoal = goalPosition == Pair(row, col)

                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(
                                            when {
                                                isPlayer -> Color.Blue // Player
                                                isGoal -> Color.Green // Goal
                                                else -> Color.Transparent // Transparent for empty cells
                                            }
                                        )
                                        .border(1.dp, Color.Black)
                                        .pointerInput(Unit) {
                                            detectDragGestures(
                                                onDragEnd = {
                                                    if (playerPosition == goalPosition) {
                                                        showWinDialog = true
                                                    }
                                                },
                                                onDrag = { _, dragAmount ->
                                                    val (dx, dy) = dragAmount
                                                    playerPosition = when {
                                                        dx > 0 && playerPosition.second < gridSize - 1 -> Pair(
                                                            playerPosition.first,
                                                            playerPosition.second + 1
                                                        )
                                                        dx < 0 && playerPosition.second > 0 -> Pair(
                                                            playerPosition.first,
                                                            playerPosition.second - 1
                                                        )
                                                        dy > 0 && playerPosition.first < gridSize - 1 -> Pair(
                                                            playerPosition.first + 1,
                                                            playerPosition.second
                                                        )
                                                        dy < 0 && playerPosition.first > 0 -> Pair(
                                                            playerPosition.first - 1,
                                                            playerPosition.second
                                                        )
                                                        else -> playerPosition
                                                    }
                                                }
                                            )
                                        }
                                ) {
                                    if (isGoal) {
                                        Text("🏁")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Win Dialog
        if (showWinDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Congratulations!") },
                text = { Text("You've reached the finish line!") },
                confirmButton = {
                    Button(onClick = { navController.navigate("level_selection") }) {
                        Text("Back to Levels")
                    }
                }
            )
        }
    }
}
