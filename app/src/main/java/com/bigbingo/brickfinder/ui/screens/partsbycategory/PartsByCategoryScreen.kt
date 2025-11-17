package com.bigbingo.brickfinder.ui.screens.partsbycategory

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import com.bigbingo.brickfinder.ui.screens.PaginationBar
import com.bigbingo.brickfinder.ui.screens.partsbycategory.components.PartsGrid
import com.bigbingo.brickfinder.ui.screens.partsbycategory.components.CategoryTopAppBar
import com.bigbingo.brickfinder.ui.screens.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartsByCategoryScreen(
    categoryId: Int,
    onBack: () -> Unit,
    viewModel: PartsViewModel = viewModel(),
    onPartClick: (String) -> Unit
) {
    val pageSize = 25
    val currentPage by viewModel.currentPage.collectAsState()
    val parts by viewModel.parts.collectAsState()
    val totalItems by viewModel.totalParts.collectAsState()
    val totalPages = (totalItems + pageSize - 1) / pageSize
    val context = LocalContext.current

    val isFirstLoad = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (parts.isEmpty()) {
            viewModel.fetchPartsPage(categoryId, currentPage, pageSize, context)
        }
    }

    LaunchedEffect(parts) {
        if (parts.isNotEmpty()) {
            isFirstLoad.value = false
        }
    }

    if (isFirstLoad.value && parts.isEmpty()) {
        LoadingScreen()
    } else {
        Scaffold(
            topBar = {
                CategoryTopAppBar(
                    totalItems = totalItems,
                    currentPage = currentPage,
                    totalPages = totalPages,
                    pageSize = pageSize,
                    onBack = {
                        viewModel.clearParts()
                        viewModel.resetCurrentPage()
                        onBack()
                    }
                )
            },
            bottomBar = {
                PaginationBar(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPageChange = { page -> viewModel.fetchPartsPage(categoryId, page, pageSize, context) },
                    modifier = Modifier.padding(bottom = 34.dp)
                )
            },
            containerColor = Color(0xffeeeeee)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                PartsGrid(parts, onPartClick = onPartClick)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}
