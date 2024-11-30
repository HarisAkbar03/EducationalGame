package com.example.educationalapp

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LevelScreen(levelId: Int) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Level $levelId", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            GameBox()
        }
    }
}

@Composable
fun GameBox() {
    var boxColor by remember { mutableStateOf(Color.Red) }

    Box(
        modifier = Modifier
            .size(200.dp)
            .background(boxColor)
            .pointerInput(Unit) {
                detectTapGestures {
                    boxColor = if (boxColor == Color.Red) Color.Green else Color.Red
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text("Tap Me", color = Color.White, fontSize = 20.sp)
    }
}
