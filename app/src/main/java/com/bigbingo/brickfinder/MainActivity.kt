package com.bigbingo.brickfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import com.bigbingo.brickfinder.data.Slider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.SlideAnimation
import com.bigbingo.brickfinder.ui.screens.BottomNavigationBar
import com.bigbingo.brickfinder.ui.screens.Screen
import com.bigbingo.brickfinder.ui.screens.partsbycategory.PartsByCategoryScreen
import com.bigbingo.brickfinder.ui.screens.home.HomeScreen
import com.bigbingo.brickfinder.ui.screens.inventory.InventoryScreen
import com.bigbingo.brickfinder.ui.screens.wantedlist.WantedListScreen
import com.bigbingo.brickfinder.ui.screens.partinfo.PartInfoScreen
import com.bigbingo.brickfinder.ui.screens.partscatalog.PartCatalogScreen
import com.bigbingo.brickfinder.ui.screens.setinfo.SetInfoScreen
import com.bigbingo.brickfinder.ui.screens.setsbytheme.SetsThemeScreen
import com.bigbingo.brickfinder.ui.screens.setscatalog.SetsCatalogScreen
import com.bigbingo.brickfinder.ui.theme.BrickFinderTheme
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import com.bigbingo.brickfinder.viewmodel.SetsViewModel

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainContent(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val navStack = remember { mutableStateListOf<Screen>(Screen.Home) }
    var isWantedListVisible by remember { mutableStateOf(false) }

    val partViewModel: PartsViewModel = viewModel()
    val setsViewModel: SetsViewModel = viewModel()

    fun navigateTo(screen: Screen) {
        navStack.add(screen)
    }

    fun popBackStack() {
        if (navStack.size > 1) {
            navStack.removeAt(navStack.lastIndex)
            val current = navStack.lastOrNull()
            if (current is Screen.Home) {
                partViewModel.clearPart()
                setsViewModel.clearSetInfo()
            }
        }
    }

    val currentScreen = navStack.last()
    val showBottomBar =  currentScreen is Screen.Home  || isWantedListVisible

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedIndex = when {
                        isWantedListVisible -> 0
                        currentScreen is Screen.Home -> 1
                        else -> 1
                    }
                ) { index ->
                    when (index) {
                        0 -> {
                            isWantedListVisible = true
                        }
                        1 -> {
                            isWantedListVisible = false
                            navStack.clear()
                            navStack.add(Screen.Home)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                is Screen.Home -> HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onNavigate = { navigateTo(it) }
                )
                is Screen.Parts -> PartCatalogScreen(
                    onCategoryClick = { navigateTo(Screen.PartsByCategory(it)) },
                    onBack = { popBackStack() }
                )
                is Screen.PartsByCategory -> PartsByCategoryScreen(
                    categoryId = currentScreen.categoryId,
                    onBack = { popBackStack() },
                    onPartClick = { partNum -> navigateTo(Screen.PartInfo(partNum)) }
                )
                is Screen.PartInfo -> PartInfoScreen(
                    partNum = currentScreen.partNum,
                    onBack = { navStack.removeAt(navStack.lastIndex) },
                    onCatalogClick = { navStack.clear(); navStack.add(Screen.Home) },
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
                    part = currentScreen.part,
                    setNums = currentScreen.setNums,
                    selectedColor = currentScreen.color,
                    onBack = { navStack.removeAt(navStack.lastIndex) },
                    onCatalogClick = {
                        navStack.clear()
                        navStack.add(Screen.Home)
                    },
                    onPartsClick = { navStack.add(Screen.Parts) },
                    onCategoryClick = { categoryId ->
                        navStack.add(Screen.PartsByCategory(categoryId))
                    },
                    onPartNumClick = {
                        navStack.add(Screen.PartInfo(currentScreen.part.part_num))
                    },
                    onSetNumClick = { setNum ->
                        navStack.add(Screen.SetInfo(setNum))
                    }
                )
                is Screen.SetsCatalog -> SetsCatalogScreen(
                    onBack = { navStack.removeAt(navStack.lastIndex) },
                    onParentClick = { parentId -> navStack.add(Screen.SetsTheme(parentId)) },
                    onChildClick = { childId -> navStack.add(Screen.SetsTheme(childId)) },
                    onSearchNavigate = { searchId -> navStack.add(Screen.SetsTheme(searchId)) }
                )
                is Screen.SetsTheme -> SetsThemeScreen(
                    themeId = currentScreen.themeId,
                    onBack = { popBackStack() }
                ) { setNum -> navigateTo(Screen.SetInfo(setNum, currentScreen.themeId)) }
                is Screen.SetInfo -> SetInfoScreen(
                    setNum = currentScreen.setNum,
                    themeId = currentScreen.themeId,
                    onBack = { popBackStack() },
                    onCatalogClick = { navStack.clear(); navStack.add(Screen.Home) },
                    onSetsClick = { navigateTo(Screen.SetsCatalog) },
                    onThemeClick = { themeId -> navStack.add(Screen.SetsTheme(themeId)) },
                    onPartNumClick = { navigateTo(Screen.PartInfo(it)) }
                )
            }
            SlideAnimation(
                visible = isWantedListVisible,
                direction = Slider.LEFT
            ) {
                WantedListScreen(
                    innerPadding = innerPadding,
                    onPartClick = { partNum -> navigateTo(Screen.PartInfo(partNum)) },
                    onSetClick = { setNum -> navigateTo(Screen.SetInfo(setNum)) }
                )
            }
        }
    }
}


