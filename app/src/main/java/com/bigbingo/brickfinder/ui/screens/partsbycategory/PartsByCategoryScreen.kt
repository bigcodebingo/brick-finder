package com.bigbingo.brickfinder.ui.screens.partsbycategory

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import com.bigbingo.brickfinder.ui.screens.PaginationBar
import com.bigbingo.brickfinder.ui.screens.partsbycategory.components.PartsGrid
import com.bigbingo.brickfinder.ui.screens.partsbycategory.components.PartsTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartsByCategoryScreen(
    categoryId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartsViewModel = viewModel()
) {
    val pageSize = 25
    var currentPage by remember { mutableIntStateOf(1) }
    val parts by viewModel.parts.collectAsState()
    val totalItems by viewModel.totalParts.collectAsState()
    val totalPages = (totalItems + pageSize - 1) / pageSize
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }


    fun loadPage(page: Int) {
        val offset = (page - 1) * pageSize
        viewModel.fetchPartsPage(categoryId, offset, pageSize, context)
        currentPage = page
    }

    LaunchedEffect(categoryId) {
        loadPage(1)
    }

    LaunchedEffect(parts) {
        if (parts.isNotEmpty()) {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            PartsTopAppBar(
                totalItems = totalItems,
                currentPage = currentPage,
                totalPages = totalPages,
                pageSize = pageSize,
                onBack = onBack
            )
        },
        bottomBar = {
            PaginationBar(
                currentPage = currentPage, totalPages = totalPages, onPageChange = { page -> loadPage(page) },
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
            PartsGrid(parts)
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
