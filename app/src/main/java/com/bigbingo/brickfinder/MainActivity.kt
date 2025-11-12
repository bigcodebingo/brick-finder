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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartColor
import com.bigbingo.brickfinder.ui.screens.Screen
import com.bigbingo.brickfinder.ui.screens.partsbycategory.PartsByCategoryScreen
import com.bigbingo.brickfinder.ui.screens.home.HomeScreen
import com.bigbingo.brickfinder.ui.screens.inventoryscreen.InventoryScreen
import com.bigbingo.brickfinder.ui.screens.myaccount.AccountScreen
import com.bigbingo.brickfinder.ui.screens.partinfo.PartInfoScreen
import com.bigbingo.brickfinder.ui.screens.partscatalog.PartScreen
import com.bigbingo.brickfinder.ui.screens.setinfo.SetInfoScreen
import com.bigbingo.brickfinder.ui.screens.setsbytheme.SetsThemeScreen
import com.bigbingo.brickfinder.ui.screens.setscatalog.SetsCatalogScreen
import com.bigbingo.brickfinder.ui.theme.BrickFinderTheme
import com.bigbingo.brickfinder.viewmodel.PartsViewModel

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
    val navStack = remember { mutableStateListOf<Screen>(Screen.Catalog) }

    fun navigateTo(screen: Screen) {
        navStack.add(screen)
    }

    fun popBackStack() {
        if (navStack.size > 1) navStack.removeAt(navStack.lastIndex)
    }

    val currentScreen = navStack.last()
    val showBottomBar = currentScreen is Screen.WantedList || currentScreen is Screen.Catalog

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedIndex = when (currentScreen) {
                        is Screen.WantedList -> 0
                        is Screen.Catalog -> 1
                        is Screen.Account -> 2
                        else -> 1
                    }
                ) { index ->
                    when (index) {
                        0 -> {
                            navStack.clear()
                            navStack.add(Screen.WantedList)
                        }
                        1 -> {
                            navStack.clear()
                            navStack.add(Screen.Catalog)
                        }
                        2 -> {
                            navStack.clear()
                            navStack.add(Screen.Account)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (val screen = currentScreen) {
                is Screen.WantedList -> HomeScreen(Modifier.padding(innerPadding)) { navigateTo(it) }
                is Screen.Catalog -> HomeScreen(Modifier.padding(innerPadding)) { navigateTo(it) }
                is Screen.Parts -> PartScreen(
                    onCategoryClick = { navigateTo(Screen.PartsByCategory(it)) },
                    onBack = { popBackStack() }
                )
                is Screen.PartsByCategory -> PartsByCategoryScreen(
                    categoryId = screen.categoryId,
                    onBack = { popBackStack() },
                    onPartClick = { partNum -> navigateTo(Screen.PartInfo(partNum)) }
                )
                is Screen.PartInfo -> PartInfoScreen(
                    partNum = screen.partNum,
                    onBack = { navStack.removeAt(navStack.lastIndex) },
                    onCatalogClick = { navStack.clear(); navStack.add(Screen.Catalog) },
                    onPartsClick = { navStack.add(Screen.Parts) },
                    onCategoryClick = { categoryId ->
                        navStack.add(Screen.PartsByCategory(categoryId))
                    },
                    onPartNumClick = {
                        navStack.lastOrNull()?.let { lastScreen ->
                            if (lastScreen is Screen.PartInfo) {
                                navStack.add(Screen.PartInfo(lastScreen.partNum))
                            }
                        }
                    },
                    onSetsClick = { part, sets ->
                        navStack.add(Screen.Inventory(part, sets))
                    },
                    onColorClick = { part, sets, color ->
                        navStack.add(Screen.Inventory(part, sets, color))
                    }
                )
                is Screen.Inventory -> InventoryScreen(
                    part = screen.part,
                    setNums = screen.setNums,
                    selectedColor = screen.color,
                    onBack = { navStack.removeAt(navStack.lastIndex) },
                    onCatalogClick = {
                        navStack.clear()
                        navStack.add(Screen.Catalog)
                    },
                    onPartsClick = { navStack.add(Screen.Parts) },
                    onCategoryClick = { categoryId ->
                        navStack.add(Screen.PartsByCategory(categoryId))
                    },
                    onPartNumClick = {
                        navStack.add(Screen.PartInfo(screen.part.part_num))
                    },
                    onSetNumClick = { setNum ->
                        navStack.add(Screen.SetInfo(setNum))
                    }
                )
                is Screen.SetsCatalog -> SetsCatalogScreen(
                    onBack = { navStack.removeAt(navStack.lastIndex) },
                    onParentClick = { parentId -> navStack.add(Screen.SetsTheme(parentId)) },
                    onChildClick = { childId -> navStack.add(Screen.SetsTheme(childId)) },
                    onSearchNavigate = { searchId ->  }
                )
                is Screen.SetsTheme -> SetsThemeScreen(
                    themeId = screen.themeId,
                    onBack = { popBackStack() }
                ) { setNum -> navigateTo(Screen.SetInfo(setNum, screen.themeId)) }
                is Screen.SetInfo -> SetInfoScreen(
                    setNum = screen.setNum,
                    themeId = screen.themeId,
                    onBack = { popBackStack() },
                    onCatalogClick = { navStack.clear(); navStack.add(Screen.Catalog) },
                    onSetsClick = { navigateTo(Screen.SetsCatalog) },
                    onThemeClick = { themeId -> navStack.add(Screen.SetsTheme(themeId)) },
                    onPartNumClick = { navigateTo(Screen.PartInfo(it)) }
                )
                is Screen.Account -> AccountScreen(innerPadding = innerPadding, onToggleTheme = onToggleTheme)
            }
        }
    }
}


