package com.bigbingo.brickfinder.ui.screens.setscatalog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.setscatalog.components.SetsTopBar
import com.bigbingo.brickfinder.ui.screens.setscatalog.components.ThemeList
import com.bigbingo.brickfinder.viewmodel.SetsViewModel
import com.bigbingo.brickfinder.ui.screens.setscatalog.components.SearchBar
import com.bigbingo.brickfinder.ui.screens.setscatalog.components.SearchResult
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetsScreen(
    viewModel: SetsViewModel = viewModel(),
    onBack: () -> Unit,
    onParentClick: (Int) -> Unit = {},
    onChildClick: (Int) -> Unit = {},
    onSearchNavigate: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val themes = remember { viewModel.fetchSetThemes(context) }
    val themeTree = remember(themes) { viewModel.buildThemeTree(themes) }

    var searchQuery by remember { mutableStateOf("") }
    var isDropdownVisible by remember { mutableStateOf(false) }
    var selectedThemeId by remember { mutableStateOf<Int?>(null) }
    var navigateToThemeId by remember { mutableStateOf<Int?>(null) }


    val searchResults = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else viewModel.searchThemes(themes, searchQuery).take(8)
    }

    LaunchedEffect(navigateToThemeId) {
        navigateToThemeId?.let { themeId ->
            delay(200)
            onSearchNavigate(themeId)
            navigateToThemeId = null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.resetCurrentPage()
        viewModel.clearSets()
    }
    Scaffold(
        topBar = {
            SetsTopBar(
                titleText = "Category Tree",
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
                        onSearchNavigate(selectedThemeId!!)
                        isDropdownVisible = false
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (isDropdownVisible) {
                    SearchResult(
                        searchResults = searchResults,
                        onResultSelected = { themeId ->
                            val resultPair = searchResults.find { (theme, _) -> theme.id == themeId }
                            resultPair?.let { (theme, parentName) ->
                                val displayName = if (parentName != null)
                                    "${theme.name} ($parentName)" else theme.name

                                searchQuery = displayName
                                selectedThemeId = theme.id
                                isDropdownVisible = false

                                navigateToThemeId = theme.id

                            }
                        },
                        modifier = Modifier.zIndex(2f)
                    )
                }

            }
        }
    }
}
