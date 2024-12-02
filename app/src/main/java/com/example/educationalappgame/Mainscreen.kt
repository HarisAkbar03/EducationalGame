package com.example.educationalappgame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.educationalappgame.ui.theme.Purple40

@Composable
fun MainScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background), // Replace with your background image resource
                contentDescription = "Background",
                contentScale = ContentScale.Crop, // Ensures the image scales and fills the screen
                modifier = Modifier.fillMaxSize()
            )

            // Semi-transparent overlay for better contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colors = listOf(Purple40.copy(alpha = 0.6f), Purple40.copy(alpha = 0.9f))))
            )

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to the Educational Game!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Register Button
                Button(
                    onClick = { navController.navigate("register") }, // Navigate to Register screen
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                ) {
                    Text(text = "Register", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Login Button
                Button(
                    onClick = { navController.navigate("login") }, // Navigate to Login screen
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                ) {
                    Text(text = "Login", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Play Button
                Button(
                    onClick = { navController.navigate("level_selection") }, // Navigate to Level Selection screen
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                ) {
                    Text(text = "Play", fontSize = 18.sp)
                }
            }
        }
    }
}
