package com.bigbingo.brickfinder.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.R
import com.bigbingo.brickfinder.data.ItemType
import com.bigbingo.brickfinder.ui.screens.Screen
import com.bigbingo.brickfinder.ui.screens.home.componets.HomeCard
import com.bigbingo.brickfinder.ui.screens.home.componets.HomeSearchResult
import com.bigbingo.brickfinder.ui.screens.homepage.componets.HomeSearchBar
import com.bigbingo.brickfinder.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier,
    onNavigate: (Screen) -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf(viewModel.searchQuery) }
    var isDropdownVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text("Browse LEGO items", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HomeCard(
                    title = "Parts",
                    countText = "59,802 items",
                    imageRes = R.drawable.catalog_parts,
                    backgroundColor = Color(0x80daeef6),
                    onClick = { onNavigate(Screen.Parts) },
                    modifier = Modifier.weight(1f),
                    imageHeight = 100.dp
                )

                HomeCard(
                    title = "Sets",
                    countText = "25,629 items",
                    imageRes = R.drawable.catalog_sets,
                    backgroundColor = Color(0x80f5efd6),
                    onClick = { onNavigate(Screen.SetsCatalog) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Search history", style = MaterialTheme.typography.bodyLarge)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            HomeSearchBar(
                value = searchQuery,
                onValueChange = { newQuery ->
                    searchQuery = newQuery
                    viewModel.onSearchQueryChange(newQuery, context)
                    isDropdownVisible = newQuery.isNotBlank()
                },
                onSearch = {
                    viewModel.searchResults.firstOrNull()?.let { item ->
                        when(item.type) {
                            ItemType.SET -> onNavigate(Screen.SetInfo(item.itemNum))
                            ItemType.PART -> onNavigate(Screen.PartInfo(item.itemNum))
                        }
                    }
                    searchQuery = ""
                    viewModel.searchQuery = ""
                    viewModel.searchResults.clear()
                    isDropdownVisible = false
                }
            )

            if (isDropdownVisible) {
                HomeSearchResult(
                    searchResults = viewModel.searchResults,
                    onResultSelected = { selectedItemNum ->
                        val item = viewModel.searchResults.find { it.itemNum == selectedItemNum }
                        item?.let {
                            searchQuery = ""
                            viewModel.searchQuery = ""
                            viewModel.searchResults.clear()
                            isDropdownVisible = false
                            when(it.type) {
                                ItemType.SET -> onNavigate(Screen.SetInfo(it.itemNum))
                                ItemType.PART -> onNavigate(Screen.PartInfo(it.itemNum))
                            }
                        }
                    },
                    modifier = Modifier.zIndex(2f).padding(top = 4.dp)
                )
            }
        }
    }
}

