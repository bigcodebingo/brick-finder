package com.bigbingo.brickfinder.ui.screens.partscatalog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.partscatalog.components.CategoryFilters
import com.bigbingo.brickfinder.ui.screens.partscatalog.components.CategoryGrid
import com.bigbingo.brickfinder.ui.screens.partscatalog.components.CategoryTopBar
import com.bigbingo.brickfinder.viewmodel.PartsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PartScreen(
    onBack: () -> Unit,
    viewModel: PartsViewModel = viewModel(),
    onCategoryClick: (Int) -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.clearParts()
        viewModel.resetCurrentPage()
        viewModel.fetchCategoriesFromDb(context)
    }

    Scaffold(
        topBar = {
            CategoryTopBar(
                titleText = "Category Tiles:",
                onBack = onBack
            )
        },
        modifier = Modifier.padding(0.dp),
        containerColor = Color(color = 0xffeeeeee)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            val selectedFilter by viewModel.selectedFilter.collectAsState()

            CategoryFilters(
                filters = listOf("Popular", "Minifigs", "Technic", "Old", "All"),
                selectedFilter = selectedFilter,
                onFilterClick = { viewModel.filterCategories(it) }
            )

            CategoryGrid(categories = categories, onCategoryClick = onCategoryClick)
        }
    }
}
