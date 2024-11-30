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
fun Game3Level1Screen(navController: NavController) {
    val gridSize = 5
    val totalQuestions = 3
    val mathProblems = listOf(
        Pair("5 + 3", 8),
        Pair("2 * 4", 8),
        Pair("6 - 2", 4)
    ).shuffled()

    var playerPosition by remember { mutableStateOf(Pair(0, 0)) }
    var questionsAnswered by remember { mutableStateOf(0) }
    var showMathDialog by remember { mutableStateOf(false) }
    var currentAnswer by remember { mutableStateOf("") }
    var currentQuestion by remember { mutableStateOf(mathProblems[0]) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Questions Solved: $questionsAnswered/$totalQuestions", style = MaterialTheme.typography.bodyLarge)

            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                Column(Modifier.fillMaxSize(), Arrangement.SpaceEvenly) {
                    repeat(gridSize) { row ->
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                            repeat(gridSize) { col ->
                                val cellPosition = Pair(row, col)
                                val isPlayer = playerPosition == cellPosition

                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(if (isPlayer) Color.Blue else Color.LightGray)
                                        .border(1.dp, Color.Black)
                                )
                            }
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.Center) {
                Button(onClick = { if (playerPosition.first > 0) playerPosition = playerPosition.copy(first = playerPosition.first - 1) }) {
                    Text("Up")
                }
                Button(onClick = { if (playerPosition.second > 0) playerPosition = playerPosition.copy(second = playerPosition.second - 1) }) {
                    Text("Left")
                }
                Button(onClick = { if (playerPosition.second < gridSize - 1) playerPosition = playerPosition.copy(second = playerPosition.second + 1) }) {
                    Text("Right")
                }
                Button(onClick = { if (playerPosition.first < gridSize - 1) playerPosition = playerPosition.copy(first = playerPosition.first + 1) }) {
                    Text("Down")
                }
            }

            Button(
                onClick = { navController.navigate("level1_game3") },
                enabled = questionsAnswered == totalQuestions
            ) {
                Text("Next Level")
            }
        }
    }

    if (showMathDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Solve the Math Problem") },
            text = { Text(currentQuestion.first) },
            confirmButton = {
                Button(onClick = {
                    if (currentAnswer.toIntOrNull() == currentQuestion.second) {
                        questionsAnswered++
                    }
                    showMathDialog = false
                }) {
                    Text("Submit")
                }
            }
        )
    }
}
