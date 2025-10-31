package com.bigbingo.brickfinder.ui.screens.inventoryscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartColor
import com.bigbingo.brickfinder.data.db.DatabaseHelper
import com.bigbingo.brickfinder.ui.screens.PartTopBar
import com.bigbingo.brickfinder.ui.screens.inventoryscreen.components.InventoryList
import com.bigbingo.brickfinder.viewmodel.PartsViewModel

@Composable
fun InventoryScreen(
    part: Part,
    setNums: List<String>,
    selectedColor: PartColor? = null,
    onBack: () -> Unit,
    onCatalogClick: () -> Unit,
    onPartsClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onPartNumClick: () -> Unit,
    viewModel: PartsViewModel = viewModel(),
    ) {

    val context = LocalContext.current
    val partAppearances by viewModel.partAppearances.collectAsState()

    val filteredAppearances = if (selectedColor != null) {
        partAppearances.filter { it.color.id == selectedColor.id }
    } else {
        partAppearances
    }
    val categoryName = remember(part) {
        part.let { p ->
            DatabaseHelper.getPartCategories(context)
                .find { it.id == p.part_cat_id }?.name
        }
    }

    LaunchedEffect(part) {
        viewModel.loadPartAppearances(context, part.part_num, setNums)
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
                .padding(innerPadding)
        ) {
            InventoryList(
                sets = filteredAppearances,
            )
        }
    }
}