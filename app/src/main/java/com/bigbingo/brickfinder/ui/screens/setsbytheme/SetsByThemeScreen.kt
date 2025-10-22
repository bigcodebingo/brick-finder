package com.bigbingo.brickfinder.ui.screens.setsbytheme

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.setsbytheme.components.SetCard
import com.bigbingo.brickfinder.ui.screens.setsbytheme.components.ThemeTopBar
import com.bigbingo.brickfinder.ui.screens.partsbycategory.components.PaginationBar
import com.bigbingo.brickfinder.viewmodel.SetsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetsThemeScreen(
    themeName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SetsViewModel = viewModel()
) {
    val pageSize = 20
    var currentPage by remember { mutableIntStateOf(1) }
    val sets by viewModel.sets.collectAsState()
    val totalSets by viewModel.totalSets.collectAsState()
    val totalPages = (totalSets + pageSize - 1) / pageSize
    val context = LocalContext.current
    var themeId by remember { mutableStateOf<Int?>(null) }

    fun loadPage(page: Int) {
        val offset = (page - 1) * pageSize
        viewModel.fetchSetsPage(context, themeId, offset, pageSize)
        currentPage = page
    }

    LaunchedEffect(themeName) {
        val themes = viewModel.fetchSetThemes(context)
        themeId = themes.find { it.name == themeName }?.id
        loadPage(1)
    }

    Scaffold(
        topBar = {
            ThemeTopBar(
                titleText = themeName,
                onBack = onBack
            )
        },
        modifier = modifier,
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            PaginationBar(currentPage, totalPages) { page -> loadPage(page) }
            Spacer(modifier = Modifier.height(6.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .scrollable(
                        state = rememberScrollState(),
                        overscrollEffect = null,
                        orientation = Orientation.Vertical),
                content = {
                    items(sets) { set ->
                        SetCard(set = set)
                    }
                }
            )
        }
    }
}