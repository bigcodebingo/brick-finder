package com.bigbingo.brickfinder.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.ui.screens.home.componets.HomeStaticCards
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
            .background(MaterialTheme.colorScheme.background)

    ) {
        SearchField(
            value = searchQuery,
            onValueChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Browse LEGO items", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        HomeStaticCards(onNavigate = onNavigate)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Search history", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))


    }
}

