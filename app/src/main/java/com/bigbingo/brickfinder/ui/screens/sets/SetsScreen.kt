package com.bigbingo.brickfinder.ui.screens.sets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.sets.components.SetsTopBar
import com.bigbingo.brickfinder.ui.screens.sets.components.ThemeList
import com.bigbingo.brickfinder.viewmodel.SetsViewModel
import com.bigbingo.brickfinder.ui.screens.sets.components.SearchBar
import com.bigbingo.brickfinder.ui.screens.sets.components.SearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetsScreen(
    viewModel: SetsViewModel = viewModel(),
    onBack: () -> Unit,
    onParentClick: (String) -> Unit = {},
    onChildClick: (String) -> Unit = {},
    onSearchNavigate: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val themes = remember { viewModel.fetchSetThemes(context) }
    val themeTree = remember(themes) { viewModel.buildThemeTree(themes) }

    var searchQuery by remember { mutableStateOf("") }
    var isDropdownVisible by remember { mutableStateOf(false) }

    val searchResults = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else viewModel.searchThemes(themes, searchQuery).take(6)
    }

    Scaffold(
        topBar = {
            SetsTopBar(
                titleText = "Find set themes:",
                onBack = onBack
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            ThemeList(
                themes = themeTree,
                onParentClick = onParentClick,
                onChildClick = onChildClick,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(0f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .zIndex(1f)
            ) {
                SearchBar(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        isDropdownVisible = it.isNotBlank()
                    },
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            onSearchNavigate(searchQuery)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (isDropdownVisible) {
                    SearchResult(
                        searchResults = searchResults,
                        onResultSelected = { selected ->
                            searchQuery = selected
                            isDropdownVisible = false
                        },
                        modifier = Modifier.zIndex(2f)
                    )
                }
            }
        }
    }
}
