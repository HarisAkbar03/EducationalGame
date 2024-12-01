package com.example.educationalappgame

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Game1Level2Screen(navController: NavController){
    val gridSize = 7 // 7x7 grid
    val path = listOf(
        Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 2), Pair(2, 2),
        Pair(2, 3), Pair(2, 4), Pair(3, 4), Pair(4, 4)
    )
    val coins = path.drop(1) // Coins appear along the path
    var playerPosition by remember { mutableStateOf(path.first()) }
    var collectedCoins by remember { mutableStateOf(0) }
    val commands = remember { mutableStateListOf<String>() }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Coins Collected: $collectedCoins/${coins.size}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Game Grid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                    repeat(gridSize) { row ->
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            repeat(gridSize) { col ->
                                val cellPosition = Pair(row, col)
                                val isPlayer = playerPosition == cellPosition
                                val isCoin = cellPosition in coins
                                val isPath = cellPosition in path

                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(
                                            when {
                                                isPlayer -> Color.Blue
                                                isCoin -> Color.Yellow
                                                isPath -> Color.Cyan
                                                else -> Color.LightGray
                                            }
                                        )
                                        .border(1.dp, Color.Black)
                                ) {
                                    if (isCoin && !isPlayer) {
                                        Text(
                                            text = "⭐",
                                            fontSize = 18.sp,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Control Panel
            ControlPanel(commands = commands)

            // Execute Commands Button
            Button(onClick = {
                executeCommands(commands, playerPosition, path, coins) { newPos, collected ->
                    playerPosition = newPos
                    collectedCoins += collected
                }
            }) {
                Text("Execute Commands")
            }
        }
    }
}

@Composable
fun ControlPanel(commands: MutableList<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("Up", "Down", "Left", "Right").forEach { command ->
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Gray)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { _, _ -> }, // No action needed while dragging
                            onDragEnd = { commands.add(command) } // Add the command when drag ends
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(command, fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

fun executeCommands(
    commands: List<String>,
    playerPosition: Pair<Int, Int>,
    path: List<Pair<Int, Int>>,
    coins: List<Pair<Int, Int>>,
    onExecute: (Pair<Int, Int>, Int) -> Unit
) {
    var currentPos = playerPosition
    var collected = 0

    commands.forEach { command ->
        val newPos = when (command) {
            "Up" -> currentPos.copy(first = currentPos.first - 1)
            "Down" -> currentPos.copy(first = currentPos.first + 1)
            "Left" -> currentPos.copy(second = currentPos.second - 1)
            "Right" -> currentPos.copy(second = currentPos.second + 1)
            else -> currentPos
        }

        if (newPos in path) {
            currentPos = newPos
            if (newPos in coins) {
                collected++
            }
        }
    }
    onExecute(currentPos, collected)
}