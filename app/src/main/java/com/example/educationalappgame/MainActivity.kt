package com.example.educationalappgame

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.educationalappgame.ui.theme.EducationalAppGameTheme

class MainActivity : ComponentActivity() {

    private lateinit var backgroundMusic: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize MediaPlayer with the background music resource
        backgroundMusic = MediaPlayer.create(this, R.raw.strangerthings).apply {
            isLooping = true
            setVolume(1.0f, 1.0f) // Max volume for both left and right channels
        }
        backgroundMusic.start()

        setContent {
            EducationalAppGameTheme {
                NavigationController()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        backgroundMusic.start() // Start music when activity becomes visible
    }

    override fun onPause() {
        super.onPause()
        backgroundMusic.pause() // Pause music when activity is no longer visible
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundMusic.release() // Release MediaPlayer resources
    }
}
