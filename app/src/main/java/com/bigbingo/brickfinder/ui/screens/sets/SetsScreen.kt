package com.bigbingo.brickfinder.ui.screens.sets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.sets.components.SetsTopBar
import com.bigbingo.brickfinder.ui.screens.sets.components.TwoColumnScrollableThemeGridWithRightExtra
import com.bigbingo.brickfinder.viewmodel.SetsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetsScreen(
    viewModel: SetsViewModel = viewModel(),
    onBack: () -> Unit,
    onParentClick: (String) -> Unit = {},
    onChildClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val themes = remember { viewModel.fetchSetThemes(context) }
    val themeTree = remember(themes) { viewModel.buildThemeTree(themes) }

    Scaffold(
        topBar = {
            SetsTopBar (
                titleText = "Find set themes:",
                onBack = onBack
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(6.dp)
                .fillMaxSize()
        ) {
            TwoColumnScrollableThemeGridWithRightExtra(
                themes = themeTree,
                onParentClick = onParentClick, // передаём обработчик
                onChildClick = onChildClick    // передаём обработчик
            )
        }
    }
}