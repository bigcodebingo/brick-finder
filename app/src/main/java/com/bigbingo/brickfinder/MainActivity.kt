package com.bigbingo.brickfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import com.bigbingo.brickfinder.ui.screens.homescreen.HomeScreen
import com.bigbingo.brickfinder.ui.theme.BrickFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrickFinderTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    var selectedIndex by remember { mutableStateOf(1) }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedIndex) { selectedIndex = it } }
    ) { innerPadding ->
        when (selectedIndex) {
            0 -> {}
            1 -> HomeScreen(modifier = Modifier.padding(innerPadding))
            2 -> {}
        }
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "WantedList") },
            selected = selectedIndex == 0,
            onClick = { onItemSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "MainPage") },
            selected = selectedIndex == 1,
            onClick = { onItemSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "MyPage") },
            selected = selectedIndex == 2,
            onClick = { onItemSelected(2) }
        )
    }
}
