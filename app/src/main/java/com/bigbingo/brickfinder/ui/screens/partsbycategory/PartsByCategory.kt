package com.bigbingo.brickfinder.ui.screens.partsbycategory

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.partsbycategory.components.PartCard
import com.bigbingo.brickfinder.viewmodel.PartsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartsByCategoryScreen(
    categoryId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartsViewModel = viewModel()
) {
    val parts by viewModel.parts.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(categoryId) {
        viewModel.fetchPartsFromDb(context, categoryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Category ID: $categoryId — ${parts.size} parts",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor =Color.Black
                )

            )
        },
        modifier = modifier,
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 75.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Vertical,
                        overscrollEffect = null
                    )
            ) {
                items(parts, key = { it.part_num }) { part ->
                    PartCard(part)
                }
            }

        }
    }
}
