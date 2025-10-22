package com.bigbingo.brickfinder.ui.screens.partsbycategory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.data.Part

@Composable
fun PartsGrid(parts: List<Part>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(parts, key = { it.part_num }) { part ->
            PartCard(part)
        }
    }
}