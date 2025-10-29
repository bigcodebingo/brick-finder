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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.data.db.DatabaseHelper
import com.bigbingo.brickfinder.ui.screens.LoadingScreen
import com.bigbingo.brickfinder.ui.screens.PartTopBar
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import kotlinx.coroutines.delay

@Composable
fun InventoryScreen(
    partNum: String,
    onBack: () -> Unit,
    viewModel: PartsViewModel = viewModel(),

    ) {


    Scaffold(
        topBar = {
            PartTopBar(
                titleText = "",
                onBack = {
                    onBack()
                }
            )
        }
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
                    text = "Information about part: ${partNum ?: "Unknown"}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}