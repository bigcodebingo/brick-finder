package com.bigbingo.brickfinder.ui.screens.parts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryFilters(
    filters: List<String>,
    selectedFilter: String,
    onFilterClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(text = "Filter:", fontSize = 13.sp, color = Color.Black)
        Spacer(modifier = Modifier.width(6.dp))

        filters.forEachIndexed { index, filter ->
            Text(
                text = filter,
                fontSize = 13.sp,
                color = if (filter == selectedFilter) Color.Black else Color(0xFF0788CA).copy(alpha = 0.8f),
                modifier = Modifier.clickable(enabled = filter != selectedFilter) { onFilterClick(filter) }
            )
            if (index != filters.lastIndex) {
                Text(text = " | ", fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}