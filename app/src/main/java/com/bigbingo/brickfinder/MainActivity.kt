package com.bigbingo.brickfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.ui.screens.partsbycategory.PartsByCategoryScreen
import com.bigbingo.brickfinder.ui.screens.home.HomeScreen
import com.bigbingo.brickfinder.ui.screens.parts.PartScreen
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
    var selectedIndex by remember { mutableIntStateOf(1) }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavigationBar(selectedIndex) { selectedIndex = it } }
    ) { innerPadding ->
        when (selectedIndex) {
            0 -> {}
            1 -> HomeScreen(
                modifier = Modifier.padding(innerPadding),
                onNavigate = { selectedIndex = it }
            )
            2 -> {}
            3 -> PartScreen(
                modifier = Modifier.padding(innerPadding),
                onCategoryClick = { categoryId ->
                    selectedCategoryId = categoryId
                    selectedIndex = 5
                },

            )
            4 -> {}
            5 -> PartsByCategoryScreen(
                categoryId = selectedCategoryId ?: 0,
                onBack = { selectedIndex = 3 }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.LightGray)
    ) {
        NavigationBar(containerColor = Color.White)
        {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Favorite, contentDescription = "Wanted List") },
                label = { Text("Wanted List") },
                selected = selectedIndex == 0,
                onClick = { onItemSelected(0) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFdbd7d7))
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Catalog") },
                label = { Text("Catalog") },
                selected = selectedIndex == 1,
                onClick = { onItemSelected(1) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFdbd7d7))
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                label = { Text("Account") },
                selected = selectedIndex == 2,
                onClick = { onItemSelected(2) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFdbd7d7))
            )
        }
    }
}
