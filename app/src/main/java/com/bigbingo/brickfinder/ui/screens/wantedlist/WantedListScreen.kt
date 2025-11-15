package com.bigbingo.brickfinder.ui.screens.wantedlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bigbingo.brickfinder.data.ItemType
import com.bigbingo.brickfinder.data.Item
import com.bigbingo.brickfinder.ui.screens.wantedlist.components.WantedItemCard
import com.bigbingo.brickfinder.viewmodel.WantedListViewModel

@Composable
fun WantedListScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    onPartClick: (String) -> Unit = {},
    onSetClick: (String) -> Unit = {},
    wantedListViewModel: WantedListViewModel = viewModel()
) {
    val context = LocalContext.current
    val wantedItems by wantedListViewModel.wantedItems.collectAsState()

    LaunchedEffect(Unit) {
        wantedListViewModel.loadWantedItems(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
    ) {
        if (wantedItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Wanted List",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Your wanted items will appear here",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Wanted List",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(wantedItems) { item ->
                    WantedItemCard(
                        item = item,
                        onItemClick = {
                            when (item.type) {
                                ItemType.PART -> onPartClick(item.itemNum)
                                ItemType.SET -> onSetClick(item.itemNum)
                            }
                        },
                        onRemoveClick = {
                            wantedListViewModel.removeFromWantedList(context, item.itemNum, item.type)
                        }
                    )
                }
            }
        }
    }
}

