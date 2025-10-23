package com.bigbingo.brickfinder.ui.screens.parts.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.data.PartCategory

@Composable
fun CategoryGrid(
    categories: List<PartCategory>,
    onCategoryClick: (Int) -> Unit

) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top= 8.dp)

    ) {
        items(categories) { category ->
            CategoryCard(category) { onCategoryClick(category.id) }
        }
    }
}