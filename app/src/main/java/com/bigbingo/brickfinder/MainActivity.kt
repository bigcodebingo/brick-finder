package com.bigbingo.brickfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartColor
import com.bigbingo.brickfinder.ui.screens.partsbycategory.PartsByCategoryScreen
import com.bigbingo.brickfinder.ui.screens.home.HomeScreen
import com.bigbingo.brickfinder.ui.screens.inventoryscreen.InventoryScreen
import com.bigbingo.brickfinder.ui.screens.myaccount.AccountScreen
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
            var isDarkTheme by remember { mutableStateOf(false) }

            BrickFinderTheme(darkTheme = isDarkTheme) {
                MainContent(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surface)

        {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Favorite, contentDescription = "Wanted List") },
                label = { Text("Wanted List") },
                selected = selectedIndex == 0,
                onClick = { onItemSelected(0) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                    indicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Catalog") },
                label = { Text("Catalog") },
                selected = selectedIndex == 1,
                onClick = { onItemSelected(1) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                    indicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                label = { Text("Account") },
                selected = selectedIndex == 2,
                onClick = { onItemSelected(2) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                    indicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                )
            )
        }
    }
}

@Composable
fun MainContent(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {

    var selectedIndex by remember { mutableIntStateOf(1) }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var selectedThemeId by remember { mutableStateOf<Int?>(null) }
    var selectedPartNum by remember { mutableStateOf("") }
    var selectedSetNum by remember { mutableStateOf("") }

    var selectedPart by remember { mutableStateOf<Part?>(null) }
    var selectedSetNums by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedPartColor by remember { mutableStateOf<PartColor?>(null) }

    var isAccountVisible by remember { mutableStateOf(false) }

    val showBottomBar = selectedIndex in listOf(0, 1, 2)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ,
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedIndex = if (isAccountVisible) 2 else selectedIndex
                ) { index ->
                    if (index == 2) {
                        isAccountVisible = true
                    } else {
                        selectedIndex = index
                        isAccountVisible = false
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {

            when (selectedIndex) {
                0 -> {}
                1 -> HomeScreen(Modifier.padding(innerPadding)) { selectedIndex = it }
                3 -> PartScreen(
                    onCategoryClick = { selectedCategoryId = it; selectedIndex = 4 },
                    onBack = { selectedIndex = 1 }
                )
                4 -> PartsByCategoryScreen(
                    categoryId = selectedCategoryId ?: 0,
                    onBack = { selectedIndex = 3 }
                ) { selectedPartNum = it; selectedIndex = 7 }
                5 -> SetsScreen(
                    onBack = { selectedIndex = 1 },
                    onParentClick = { selectedThemeId = it; selectedIndex = 6 },
                    onChildClick = { selectedThemeId = it; selectedIndex = 6 },
                    onSearchNavigate = { selectedThemeId = it; selectedIndex = 6 }
                )
                6 -> SetsThemeScreen(
                    onBack = { selectedIndex = 5 },
                    themeId = selectedThemeId ?: 0
                ) { selectedSetNum = it; selectedIndex = 8 }
                7 -> PartInfoScreen(
                    partNum = selectedPartNum,
                    onBack = { selectedIndex = 4 },
                    onSetsClick = { part, sets ->
                        selectedPart = part
                        selectedSetNums = sets
                        selectedPartColor = null
                        selectedIndex = 9
                    },
                    onColorClick = { part, sets, color ->
                        selectedPart = part
                        selectedSetNums = sets
                        selectedPartColor = color
                        selectedIndex = 9
                    },
                    onCatalogClick = { selectedIndex = 1 },
                    onPartsClick = { selectedIndex = 3 },
                    onCategoryClick = { selectedIndex = 4 },
                    onPartNumClick = { selectedIndex = 7 },
                )
                8 -> SetInfoScreen(setNum = selectedSetNum, onBack = { selectedIndex = 6 })
                9 -> selectedPart?.let { part ->
                    InventoryScreen(
                        part = part,
                        setNums = selectedSetNums,
                        selectedColor = selectedPartColor,
                        onBack = { selectedIndex = 7 },
                        onCatalogClick = { selectedIndex = 1 },
                        onPartsClick = { selectedIndex = 3 },
                        onCategoryClick = { selectedIndex = 4 },
                        onPartNumClick = { selectedIndex = 7 },
                        onSetNumClick = { setNum ->
                            selectedSetNum = setNum
                            selectedIndex = 8
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = isAccountVisible,
                enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)),
                exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .pointerInput(Unit) {  }
                ) {
                    AccountScreen(
                        innerPadding = innerPadding,
                        onToggleTheme = onToggleTheme
                    )
                }
            }
        }
    }
}


