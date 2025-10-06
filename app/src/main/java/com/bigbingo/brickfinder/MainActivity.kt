package com.bigbingo.brickfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bigbingo.brickfinder.ui.screens.HomeScreen
import com.bigbingo.brickfinder.ui.theme.BrickFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrickFinderTheme {
                HomeScreen()
            }
        }
    }
}
