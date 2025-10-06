package com.bigbingo.brickfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.data.LegoPart
import com.bigbingo.brickfinder.data.LegoSet
import com.bigbingo.brickfinder.data.PartCategory
import com.bigbingo.brickfinder.ui.components.BottomNavigationBar
import com.bigbingo.brickfinder.ui.components.CategoryCard
import com.bigbingo.brickfinder.ui.components.PartCard
import com.bigbingo.brickfinder.ui.components.SearchField
import com.bigbingo.brickfinder.ui.components.SetCard

@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            var searchQuery by remember { mutableStateOf("") }

            SearchField(
                value = searchQuery,
                onValueChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Categories", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            val categories = List(10) { index ->
                PartCategory(id = index, name = "Category ${index + 1}")
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(categories) { category ->
                    CategoryCard(category)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Search history", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            val history = listOf(
                LegoPart(1, "Brick 2x4", "Red", "", 2, "Bricks", "1995-2000"),
                LegoPart(2, "Brick 2x3", "Blue", "", 3, "Bricks", "1996-2001"),
                LegoPart(3, "Wheel 30mm", "Black", "", 5, "Wheels", "2005"),
                LegoPart(4, "Wheel 24mm", "Black", "", 4, "Wheels", "2006"),
                LegoPart(5, "Door 1x4x6", "White", "", 6, "Doors", "2000-2005")
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(history) { part ->
                    PartCard(part)
                }
            }
        }
    }
}