package com.example.educationalappgame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun Game1Level1Screen(navController: NavController) {
    val gridSize = 5 // 5x5 Grid
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Calculate the size of each cell dynamically
    val cellSize: Dp = (screenWidth.coerceAtMost(screenHeight) - 32.dp) / gridSize
    val cellSizePx = with(LocalDensity.current) { cellSize.toPx() }

    val goalPosition = Pair(4, 4) // Goal at bottom-right corner
    val obstacleCount = 5 // Number of obstacles
    val playerStartPosition = Pair(0, 0)

    // Generate obstacles while avoiding the player and goal positions
    val obstacles = remember {
        val obstacleSet = mutableSetOf<Pair<Int, Int>>()
        while (obstacleSet.size < obstacleCount) {
            val obstacle = Pair(Random.nextInt(gridSize), Random.nextInt(gridSize))
            if (obstacle != playerStartPosition && obstacle != goalPosition) {
                obstacleSet.add(obstacle)
            }
        }
        obstacleSet.toSet()
    }

    var playerPosition by remember { mutableStateOf(playerStartPosition) }
    var dragOffsetX by remember { mutableStateOf(0f) }
    var dragOffsetY by remember { mutableStateOf(0f) }
    var showWinDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Background Image
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.area1), // Replace with your image resource
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Game Content
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Game Grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(horizontal = 16.dp),
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
                                    val currentCell = Pair(row, col)
                                    val isPlayerPosition = playerPosition == currentCell
                                    val isObstacle = currentCell in obstacles

                                    Box(
                                        modifier = Modifier
                                            .size(cellSize)
                                            .background(
                                                when {
                                                    isPlayerPosition -> Color.Blue // Player
                                                    isObstacle -> Color.Red // Obstacle
                                                    goalPosition == currentCell -> Color.Green // Goal
                                                    else -> Color.LightGray // Empty Cell
                                                }
                                            )
                                            .border(1.dp, Color.Black)
                                            // Add drag functionality directly to the player's box
                                            .pointerInput(Unit) {
                                                if (isPlayerPosition) {
                                                    detectDragGestures(
                                                        onDragEnd = {
                                                            dragOffsetX = 0f
                                                            dragOffsetY = 0f
                                                        },
                                                        onDrag = { change, dragAmount ->
                                                            change.consume()
                                                            dragOffsetX += dragAmount.x
                                                            dragOffsetY += dragAmount.y

                                                            // Movement is triggered with smaller drag thresholds (half of cell size)
                                                            if (abs(dragOffsetX) > cellSizePx / 2) {
                                                                val targetPosition = if (dragOffsetX > 0 && playerPosition.second < gridSize - 1) {
                                                                    Pair(playerPosition.first, playerPosition.second + 1)
                                                                } else if (dragOffsetX < 0 && playerPosition.second > 0) {
                                                                    Pair(playerPosition.first, playerPosition.second - 1)
                                                                } else playerPosition

                                                                // Move only if target position is not an obstacle
                                                                if (targetPosition !in obstacles) {
                                                                    playerPosition = targetPosition
                                                                }
                                                                dragOffsetX = 0f
                                                            }

                                                            if (abs(dragOffsetY) > cellSizePx / 2) {
                                                                val targetPosition = if (dragOffsetY > 0 && playerPosition.first < gridSize - 1) {
                                                                    Pair(playerPosition.first + 1, playerPosition.second)
                                                                } else if (dragOffsetY < 0 && playerPosition.first > 0) {
                                                                    Pair(playerPosition.first - 1, playerPosition.second)
                                                                } else playerPosition

                                                                // Move only if target position is not an obstacle
                                                                if (targetPosition !in obstacles) {
                                                                    playerPosition = targetPosition
                                                                }
                                                                dragOffsetY = 0f
                                                            }
                                                        }
                                                    )
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (goalPosition == currentCell) {
                                            Text(text = "🏁", fontSize = 18.sp) // Goal marker
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Check for Win Condition
    LaunchedEffect(playerPosition) {
        if (playerPosition == goalPosition) {
            showWinDialog = true
        }
    }

    // Win Dialog
    if (showWinDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Congratulations!") },
            text = { Text("You reached the goal!") },
            confirmButton = {
                Button(onClick = { navController.navigate("level1_game2") }) {
                    Text("Next Level")
                }
            }
        )
    }
}
