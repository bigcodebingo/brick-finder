package com.bigbingo.brickfinder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbingo.brickfinder.viewmodel.PartsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartTopBar(
    partNum: String? = null,
    categoryName: String? = null,
    onBack: () -> Unit,
    onCatalogClick: () -> Unit,
    onPartsClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onPartNumClick: () -> Unit,
    showInSets: Boolean = true,
    viewModel: PartsViewModel? = null
) {
    TopAppBar(
        title = {
            FlowRow(
                maxLines = 2,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Catalog: ",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        onCatalogClick()
                        viewModel?.clearPart()
                        viewModel?.clearParts()
                        viewModel?.resetCurrentPage()
                    }
                )
                Text(
                    text = "Parts: ",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        onPartsClick()
                        viewModel?.clearPart()
                        viewModel?.clearParts()
                    }
                )
                categoryName?.let {
                    Text(
                        text = "${categoryName}: ",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            onCategoryClick()
                            viewModel?.clearPart()
                        }
                    )
                }
                partNum?.let {
                    Text(
                        text = "${partNum}: ",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onPartNumClick() }
                    )
                }
                if (showInSets) {
                    Text(
                        text = "In sets",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF506070),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}