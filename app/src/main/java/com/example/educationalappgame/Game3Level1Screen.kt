package com.example.educationalappgame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var currentAnswer by remember { mutableStateOf(TextFieldValue("")) }
    var currentQuestion by remember { mutableStateOf(mathProblems[0]) }
    val cellsWithQuestions = remember {
        val questionCells = mutableSetOf<Pair<Int, Int>>()
        while (questionCells.size < totalQuestions) {
            questionCells.add(
                Pair((0 until gridSize).random(), (0 until gridSize).random())
            )
        }
        questionCells
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Questions Solved
            Text(
                text = "Questions Solved: $questionsAnswered/$totalQuestions",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Game Grid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(gridSize) { row ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(gridSize) { col ->
                                val cellPosition = Pair(row, col)
                                val isPlayer = playerPosition == cellPosition
                                val hasQuestion = cellPosition in cellsWithQuestions

                                // Display Cell
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(
                                            when {
                                                isPlayer -> Color.Blue // Player
                                                hasQuestion -> Color.Yellow // Question
                                                else -> Color.LightGray // Empty Cell
                                            }
                                        )
                                        .border(1.dp, Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (hasQuestion && !isPlayer) {
                                        Text("?", fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Movement Controls
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Up Button
                Button(
                    onClick = {
                        if (playerPosition.first > 0) {
                            playerPosition = playerPosition.copy(first = playerPosition.first - 1)
                            checkForQuestion(playerPosition, cellsWithQuestions, { showMathDialog = true })
                        }
                    }
                ) {
                    Text("Up")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Left Button
                    Button(
                        onClick = {
                            if (playerPosition.second > 0) {
                                playerPosition = playerPosition.copy(second = playerPosition.second - 1)
                                checkForQuestion(playerPosition, cellsWithQuestions, { showMathDialog = true })
                            }
                        }
                    ) {
                        Text("Left")
                    }

                    // Right Button
                    Button(
                        onClick = {
                            if (playerPosition.second < gridSize - 1) {
                                playerPosition = playerPosition.copy(second = playerPosition.second + 1)
                                checkForQuestion(playerPosition, cellsWithQuestions, { showMathDialog = true })
                            }
                        }
                    ) {
                        Text("Right")
                    }
                }

                // Down Button
                Button(
                    onClick = {
                        if (playerPosition.first < gridSize - 1) {
                            playerPosition = playerPosition.copy(first = playerPosition.first + 1)
                            checkForQuestion(playerPosition, cellsWithQuestions, { showMathDialog = true })
                        }
                    }
                ) {
                    Text("Down")
                }
            }

            // Next Level Button
            Button(
                onClick = { navController.navigate("level1_game3") },
                enabled = questionsAnswered == totalQuestions,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Next Level")
            }
        }
    }

    // Math Problem Dialog
    if (showMathDialog) {
        AlertDialog(
            onDismissRequest = { showMathDialog = false },
            title = { Text("Solve the Math Problem") },
            text = {
                Column {
                    Text(currentQuestion.first)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = currentAnswer,
                        onValueChange = { currentAnswer = it },
                        label = { Text("Your Answer") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (currentAnswer.text.toIntOrNull() == currentQuestion.second) {
                            questionsAnswered++
                            cellsWithQuestions.remove(playerPosition)
                            if (questionsAnswered < totalQuestions) {
                                currentQuestion = mathProblems[questionsAnswered]
                            }
                        }
                        currentAnswer = TextFieldValue("")
                        showMathDialog = false
                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                Button(onClick = { showMathDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Function to check for a question at the current position
private fun checkForQuestion(
    playerPosition: Pair<Int, Int>,
    cellsWithQuestions: MutableSet<Pair<Int, Int>>,
    onQuestionFound: () -> Unit
) {
    if (playerPosition in cellsWithQuestions) {
        onQuestionFound()
    }
}
