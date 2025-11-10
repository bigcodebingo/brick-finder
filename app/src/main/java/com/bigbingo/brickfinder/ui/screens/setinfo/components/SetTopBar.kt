package com.bigbingo.brickfinder.ui.screens.setinfo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbingo.brickfinder.viewmodel.SetsViewModel
import kotlin.text.dropLast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetTopBar(
    setNum: String? = null,
    themeName: String? = null,
    onBack: () -> Unit,
    onCatalogClick: () -> Unit,
    onSetsClick: () -> Unit,
    onThemeClick: () -> Unit,
    viewModel: SetsViewModel? = null
) {
    TopAppBar(
        title = {
            FlowRow(
                maxLines = 2,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Catalog:",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        onCatalogClick()
                        viewModel?.clearSetInfo()
                        viewModel?.clearSets()
                        viewModel?.resetCurrentPage()
                    }
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Sets:",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        onSetsClick()
                        viewModel?.clearSetInfo()
                        viewModel?.clearSets()
                    }
                )
                if (!themeName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$themeName:",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            onThemeClick()
                            viewModel?.clearSetInfo()
                        }
                    )
                }
                if (!setNum.isNullOrBlank()) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = setNum.dropLast(2),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp)
                    )
                }
            }
        },
        expandedHeight = 50.dp,
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