package com.bigbingo.brickfinder.ui.screens.setinfo

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bigbingo.brickfinder.viewmodel.SetsViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetInfoScreen(
    setNum: String,
    onBack: () -> Unit,
    viewModel: SetsViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val year by viewModel.setYear.collectAsState()
    val numParts by viewModel.setNumParts.collectAsState()
    val inventories by viewModel.setInventories.collectAsState()
    val selectedInventory by viewModel.selectedInventory.collectAsState()

    LaunchedEffect(setNum) {
        viewModel.loadSetInfo(context, setNum)
    }

    val currentInventory = selectedInventory

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set: $setNum") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if ((year == null && numParts == null) || currentInventory == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    // Информация о наборе
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Year: ${year ?: "—"}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Number of parts: ${numParts ?: "—"}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (inventories.size > 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                val currentIndex = inventories.indexOf(currentInventory)
                                val nextIndex = (currentIndex + 1) % inventories.size
                                viewModel.selectInventory(nextIndex)
                            }) {
                                Text("Inventory ${currentInventory.version} (Click to switch)")
                            }
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Inventory ${currentInventory.version}")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val allParts = currentInventory.parts +
                            currentInventory.minifigParts.map { Triple(it.first, it.second, it.third) }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        if (currentInventory.parts.isNotEmpty()) {
                            item(span = { GridItemSpan(5) }) {
                                Text(
                                    text = "Parts (total ${currentInventory.parts.sumOf { it.third }})",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(currentInventory.parts) { (partNum, imgUrl, qty) ->
                                Column(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(100.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = imgUrl,
                                        contentDescription = partNum,
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = partNum,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "x$qty",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        if (currentInventory.minifigs.isNotEmpty()) {
                            item(span = { GridItemSpan(5) }) {
                                Text(
                                    text = "Minifigs (total ${currentInventory.minifigs.sumOf { it.third }})",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(currentInventory.minifigs) { (figNum, imgUrl, qty) ->
                                Column(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(100.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = imgUrl,
                                        contentDescription = figNum,
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = figNum,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "x$qty",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        if (currentInventory.minifigParts.isNotEmpty()) {
                            item(span = { GridItemSpan(5) }) {
                                Text(
                                    text = "Minifig Parts (total ${currentInventory.minifigParts.sumOf { it.third }})",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(currentInventory.minifigParts) { (partNum, imgUrl, qty) ->
                                Column(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(100.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = imgUrl,
                                        contentDescription = partNum,
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = partNum,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "x$qty",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
