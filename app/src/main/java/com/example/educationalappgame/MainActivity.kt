package com.example.educationalappgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.educationalappgame.ui.theme.EducationalAppGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EducationalAppGameTheme {
                NavigationController()
            }
        }
    }
}
