package com.example.educationalappgame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random

@Composable
fun Game1Level2Screen(navController: NavController) {
    val puzzlePieces = listOf("Engine", "Body", "Wings")
    val targetSlots = listOf("Engine Slot", "Body Slot", "Wings Slot")
    val placements = remember { mutableStateMapOf<String, String>() } // Tracks correct placements

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Drag and Drop Puzzle",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Target Slots
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                targetSlots.forEach { slot ->
                    Box(
                        modifier = Modifier
                            .size(120.dp, 50.dp)
                            .background(
                                if (placements.containsValue(slot)) Color.Green else Color.LightGray
                            )
                            .border(1.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(slot)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Draggable Pieces
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                puzzlePieces.forEach { piece ->
                    Box(
                        modifier = Modifier
                            .size(120.dp, 50.dp)
                            .background(Color.Blue)
                            .border(1.dp, Color.Black)
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    val draggedTo = Random.nextInt(targetSlots.size) // Simulating a drop
                                    if (targetSlots[draggedTo] == "$piece Slot") {
                                        placements[piece] = targetSlots[draggedTo]
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(piece, color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next Button
            Button(
                onClick = { navController.navigate("level2_game3") },
                enabled = placements.size == puzzlePieces.size
            ) {
                Text("Next Level")
            }
        }
    }
}
