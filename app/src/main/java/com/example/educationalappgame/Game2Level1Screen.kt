package com.example.educationalappgame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import androidx.navigation.NavController

@Composable
fun Game2Level1Screen(navController: NavController) {
    val gridSize = 5 // 5x5 Grid
    val totalStars = 5 // Number of stars to collect

    var playerPosition by remember { mutableStateOf(Pair(0, 0)) }
    var starsCollected by remember { mutableStateOf(0) }
    val stars = remember {
        val starSet = mutableSetOf<Pair<Int, Int>>()
        while (starSet.size < totalStars) {
            val randomPosition = Pair(Random.nextInt(gridSize), Random.nextInt(gridSize))
            if (randomPosition != Pair(0, 0)) { // Avoid placing a star on the player's starting position
                starSet.add(randomPosition)
            }
        }
        starSet.toMutableSet()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text for stars collected
            Text(
                text = "Stars Collected: $starsCollected/$totalStars",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Game Grid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(gridSize) { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(gridSize) { col ->
                                val cellPosition = Pair(row, col)
                                val isPlayer = playerPosition == cellPosition
                                val isStar = cellPosition in stars

                                Box(
                                    modifier = Modifier
                                        .size(50.dp) // Adjusted size to fit better
                                        .background(
                                            when {
                                                isPlayer -> Color.Blue // Player
                                                isStar -> Color.Yellow // Star
                                                else -> Color.LightGray // Empty cell
                                            }
                                        )
                                        .border(1.dp, Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isStar) {
                                        Text(text = "⭐", fontSize = 18.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (playerPosition.first > 0) {
                            playerPosition = playerPosition.copy(first = playerPosition.first - 1)
                            checkStarCollection(playerPosition, stars) { starsCollected++ }
                        }
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Move Up")
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (playerPosition.second > 0) {
                                playerPosition = playerPosition.copy(second = playerPosition.second - 1)
                                checkStarCollection(playerPosition, stars) { starsCollected++ }
                            }
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("Move Left")
                    }

                    Button(
                        onClick = {
                            if (playerPosition.second < gridSize - 1) {
                                playerPosition = playerPosition.copy(second = playerPosition.second + 1)
                                checkStarCollection(playerPosition, stars) { starsCollected++ }
                            }
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("Move Right")
                    }
                }
                Button(
                    onClick = {
                        if (playerPosition.first < gridSize - 1) {
                            playerPosition = playerPosition.copy(first = playerPosition.first + 1)
                            checkStarCollection(playerPosition, stars) { starsCollected++ }
                        }
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Move Down")
                }
            }

            // Next Button
            Button(
                onClick = { navController.navigate("level1_game3") },
                enabled = starsCollected == totalStars,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Next Game")
            }
        }
    }
}

// Helper function to check and collect stars
private fun checkStarCollection(
    playerPosition: Pair<Int, Int>,
    stars: MutableSet<Pair<Int, Int>>,
    onStarCollected: () -> Unit
) {
    if (playerPosition in stars) {
        stars.remove(playerPosition)
        onStarCollected()
    }
}
