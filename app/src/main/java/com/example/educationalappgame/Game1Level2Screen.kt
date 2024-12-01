package com.example.educationalappgame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import androidx.navigation.NavController

@Composable
fun Game1Level2Screen(navController: NavController) {
    val gridSize = 5 // 5x5 Grid
    val totalStars = 5 // Number of stars to collect
    var playerPosition by remember { mutableStateOf(Pair(0, 0)) } // Player starts at (0, 0)
    var starsCollected by remember { mutableStateOf(0) }

    // Generate random star positions
    val stars = remember {
        val starSet = mutableSetOf<Pair<Int, Int>>()
        while (starSet.size < totalStars) {
            val randomPosition = Pair(Random.nextInt(gridSize), Random.nextInt(gridSize))
            if (randomPosition != Pair(0, 0)) { // Avoid starting position
                starSet.add(randomPosition)
            }
        }
        starSet.toMutableSet()
    }

    // Get screen dimensions and calculate cell size
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val gridSizeDp = (screenWidth.coerceAtMost(screenHeight) - 32.dp) // Maximum available size for the grid
    val cellSize = gridSizeDp / gridSize // Dynamically calculate the size of each cell

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Stars collected
            Text(
                text = "Stars Collected: $starsCollected/$totalStars",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Game grid
            Box(
                modifier = Modifier
                    .size(gridSizeDp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Column(
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

                                // Display cell
                                Box(
                                    modifier = Modifier
                                        .size(cellSize)
                                        .background(
                                            when {
                                                isPlayer -> Color.Blue // Player
                                                isStar -> Color.Yellow // Star
                                                else -> Color.LightGray // Empty Cell
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

            // Movement controls
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Up Button
                Button(onClick = {
                    if (playerPosition.first > 0) {
                        playerPosition = playerPosition.copy(first = playerPosition.first - 1) // Move up
                        collectStar(playerPosition, stars) { starsCollected++ }
                    }
                }) {
                    Text("Move Up")
                }

                // Left and Right Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        if (playerPosition.second > 0) {
                            playerPosition = playerPosition.copy(second = playerPosition.second - 1) // Move left
                            collectStar(playerPosition, stars) { starsCollected++ }
                        }
                    }) {
                        Text("Move Left")
                    }

                    Button(onClick = {
                        if (playerPosition.second < gridSize - 1) {
                            playerPosition = playerPosition.copy(second = playerPosition.second + 1) // Move right
                            collectStar(playerPosition, stars) { starsCollected++ }
                        }
                    }) {
                        Text("Move Right")
                    }
                }

                // Down Button
                Button(onClick = {
                    if (playerPosition.first < gridSize - 1) {
                        playerPosition = playerPosition.copy(first = playerPosition.first + 1) // Move down
                        collectStar(playerPosition, stars) { starsCollected++ }
                    }
                }) {
                    Text("Move Down")
                }
            }

            // Next level button
            Button(
                onClick = { navController.navigate("level1_game2") },
                enabled = starsCollected == totalStars,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Next Level")
            }
        }
    }
}

// Function to collect stars
private fun collectStar(
    playerPosition: Pair<Int, Int>,
    stars: MutableSet<Pair<Int, Int>>,
    onStarCollected: () -> Unit
) {
    if (playerPosition in stars) {
        stars.remove(playerPosition)
        onStarCollected()
    }
}
