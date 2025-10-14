package com.bigbingo.brickfinder.ui.screens.partscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.partscreen.components.CategoryCard
import com.bigbingo.brickfinder.viewmodel.PartsViewModel

@Composable
fun PartScreen(
    modifier: Modifier = Modifier,
    viewModel: PartsViewModel = viewModel()
) {
    val context = LocalContext.current
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCategoriesFromCsv(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Category Tiles:",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (categories.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { category ->
                    CategoryCard(category)
                }
            }
        }
    }
}
