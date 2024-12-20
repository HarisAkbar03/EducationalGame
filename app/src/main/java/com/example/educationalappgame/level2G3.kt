package com.example.educationalappgame

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Game3Level2Screen(navController: NavController) {
    val customGrid = listOf(
        listOf(1, 1, 1, 1, 1),
        listOf(1, 0, 0, 0, 0),
        listOf(1, 1, 1, 1, 1),
        listOf(0, 0, 0, 0, 1),
        listOf(1, 1, 1, 1, 1)
    )
    val gridSize = customGrid.size
    val cellSize = 60.dp

    // Starting position: bottom-left corner of the "S"
    val startingPosition = Pair(4, 0)
    // Goal position: top-right corner of the "S"
    val goalPosition = Pair(0, 4)

    var playerPosition by remember { mutableStateOf(startingPosition) }
    var showWinDialog by remember { mutableStateOf(false) }

    val commands = listOf("Up", "Down", "Left", "Right")
    val commandArea = remember { mutableStateListOf<String>() } // Holds dropped commands
    val coroutineScope = rememberCoroutineScope()

    fun executeCommands() {
        coroutineScope.launch {
            var newPlayerPosition = playerPosition
            for (command in commandArea) {
                while (true) {
                    val nextPosition = when (command) {
                        "Up" -> Pair(newPlayerPosition.first - 1, newPlayerPosition.second)
                        "Down" -> Pair(newPlayerPosition.first + 1, newPlayerPosition.second)
                        "Left" -> Pair(newPlayerPosition.first, newPlayerPosition.second - 1)
                        "Right" -> Pair(newPlayerPosition.first, newPlayerPosition.second + 1)
                        else -> newPlayerPosition
                    }

                    // Check bounds and if the next cell is valid
                    if (
                        nextPosition.first !in 0 until gridSize || // Row bounds check
                        nextPosition.second !in 0 until gridSize || // Column bounds check
                        customGrid[nextPosition.first][nextPosition.second] != 1 // Path check
                    ) {
                        break // Stop moving in this direction if invalid
                    }

                    newPlayerPosition = nextPosition
                    playerPosition = newPlayerPosition

                    // Add a delay for slower movement
                    delay(700L)

                    // Check for goal
                    if (newPlayerPosition == goalPosition) {
                        showWinDialog = true
                        break
                    }
                }
            }
            commandArea.clear() // Clear commands after execution
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.gb), // Replace with your image resource
                contentDescription = "Game Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Game Grid and Player
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Game Grid
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
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
                                    val isPath = customGrid[row][col] == 1
                                    val isPlayer = Pair(row, col) == playerPosition
                                    val isGoal = Pair(row, col) == goalPosition

                                    Box(
                                        modifier = Modifier
                                            .size(cellSize)
                                            .background(
                                                when {
                                                    isPlayer -> Color.Transparent // Transparent for circle player
                                                    isGoal -> Color.Green // Goal
                                                    isPath -> Color.Cyan // Path
                                                    else -> Color.Transparent // Non-path areas
                                                }
                                            )
                                    ) {
                                        if (isPlayer) {
                                            // Player Circle
                                            Box(
                                                modifier = Modifier
                                                    .size(cellSize)
                                                    .background(Color.Blue, CircleShape)
                                            )
                                        }

                                        if (isGoal) {
                                            Text("🏁", color = Color.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Command Buttons and Drop Area
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Command Buttons
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        commands.forEach { command ->
                            Box(
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(30.dp)
                                    .background(Color.Gray)
                                    .pointerInput(Unit) {
                                        detectDragGestures { change, _ ->
                                            change.consume()
                                            if (!commandArea.contains(command)) {
                                                commandArea.add(command)
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(command, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Small Drop Area
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(100.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            commandArea.forEach { command ->
                                Text(command, color = Color.Black)
                            }
                        }
                    }
                }

                // Play Button to Execute Commands
                Button(
                    onClick = { executeCommands() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Play")
                }
            }
        }

        // Win Dialog
        if (showWinDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Congratulations!") },
                text = { Text("You reached the goal!") },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { navController.navigate("level_selection") }) {
                            Text("Go to Level Selection")
                        }
                        Button(onClick = { (navController.context as? Activity)?.finish() }) {
                            Text("Exit Game")
                        }
                    }
                }
            )
        }
    }
}
