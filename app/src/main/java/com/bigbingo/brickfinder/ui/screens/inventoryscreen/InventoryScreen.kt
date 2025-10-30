package com.bigbingo.brickfinder.ui.screens.inventoryscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.db.DatabaseHelper
import com.bigbingo.brickfinder.ui.screens.PartTopBar
import com.bigbingo.brickfinder.viewmodel.PartsViewModel

@Composable
fun InventoryScreen(
    part: Part,
    onBack: () -> Unit,
    onCatalogClick: () -> Unit,
    onPartsClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onPartNumClick: () -> Unit,
    viewModel: PartsViewModel = viewModel(),
    ) {

    val context = LocalContext.current
    val categoryName = remember(part) {
        part.let { p ->
            DatabaseHelper.getPartCategories(context)
                .find { it.id == p.part_cat_id }?.name
        }
    }

    Scaffold(
        topBar = {
            PartTopBar(
                partNum = part.part_num,
                categoryName = categoryName,
                onBack = onBack,
                onCatalogClick = onCatalogClick,
                onPartsClick = onPartsClick,
                onCategoryClick = onCategoryClick,
                onPartNumClick = onPartNumClick,
                viewModel = viewModel
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Information about part: ${part.part_num}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.DarkGray
                )
                Text(
                    text = "Name: ${part.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
    }
}