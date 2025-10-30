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
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.ui.screens.partsbycategory.PartsByCategoryScreen
import com.bigbingo.brickfinder.ui.screens.home.HomeScreen
import com.bigbingo.brickfinder.ui.screens.inventoryscreen.InventoryScreen
import com.bigbingo.brickfinder.ui.screens.partinfo.PartInfoScreen
import com.bigbingo.brickfinder.ui.screens.partscatalog.PartScreen
import com.bigbingo.brickfinder.ui.screens.setinfo.SetInfoScreen
import com.bigbingo.brickfinder.ui.screens.setscatalog.SetsScreen
import com.bigbingo.brickfinder.ui.screens.setsbytheme.SetsThemeScreen
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
    var selectedThemeId by remember { mutableStateOf<Int?>(null) }
    var selectedPartNum by remember { mutableStateOf("") }
    var selectedSetNum by remember { mutableStateOf("") }

    var selectedPart by remember { mutableStateOf<Part?>(null) }
    val showBottomBar = selectedIndex in listOf(0, 1, 2)

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(selectedIndex) { selectedIndex = it }
            }
        }
    ) { innerPadding ->
        when (selectedIndex) {
            0 -> {}
            1 -> HomeScreen(
                modifier = Modifier.padding(innerPadding),
                onNavigate = { selectedIndex = it }
            )
            2 -> {}
            3 -> PartScreen(
                onCategoryClick = { categoryId ->
                    selectedCategoryId = categoryId
                    selectedIndex = 4
                },
                onBack = { selectedIndex = 1 }
            )
            4 -> PartsByCategoryScreen(
                categoryId = selectedCategoryId ?: 0,
                onBack = { selectedIndex = 3 },
                onPartClick = { partNum ->
                    selectedPartNum = partNum
                    selectedIndex = 7
                }
            )
            5 -> SetsScreen(
                onBack = { selectedIndex = 1 },
                onParentClick = { themeId  ->
                    selectedThemeId = themeId
                    selectedIndex = 6
                },
                onChildClick = { themeId ->
                    selectedThemeId = themeId
                    selectedIndex = 6
                },
                onSearchNavigate = { themeId ->
                    selectedThemeId = themeId
                    selectedIndex = 6
                }
            )
            6 -> SetsThemeScreen(
                onBack = { selectedIndex = 5 },
                themeId = selectedThemeId ?: 0,
                onSetClick = { setNum ->
                    selectedSetNum = setNum
                    selectedIndex = 8
                }
            )
            7 -> PartInfoScreen(
                partNum = selectedPartNum,
                onBack = { selectedIndex = 4 },
                onClickSets = { part ->
                    selectedPart = part 
                    selectedIndex = 9
                },
                onCatalogClick = { selectedIndex = 1 },
                onPartsClick = { selectedIndex = 3 },
                onCategoryClick = { selectedIndex = 4 },
                onPartNumClick = { selectedIndex = 7 },
            )
            8 -> SetInfoScreen(
                setNum = selectedSetNum,
                onBack = { selectedIndex = 6 }
            )
            9 -> {
                selectedPart?.let { part ->
                    InventoryScreen(
                        part = selectedPart!!,
                        onBack = { selectedIndex = 7 },
                        onCatalogClick = { selectedIndex = 1 },
                        onPartsClick = { selectedIndex = 3 },
                        onCategoryClick = { selectedIndex = 4 },
                        onPartNumClick = { selectedIndex = 7 },
                    )
                }
            }
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