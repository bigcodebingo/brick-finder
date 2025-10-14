package com.bigbingo.brickfinder.ui.screens.homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.data.LegoPart
import com.bigbingo.brickfinder.ui.screens.homescreen.componets.HomeStaticCards
import com.bigbingo.brickfinder.ui.screens.homepage.componets.SearchField

@Composable
fun HomeScreen(
    modifier: Modifier,
    onNavigate: (Int) -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        SearchField(
            value = searchQuery,
            onValueChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Browse LEGO items", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        HomeStaticCards(onNavigate = onNavigate)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Search history", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

