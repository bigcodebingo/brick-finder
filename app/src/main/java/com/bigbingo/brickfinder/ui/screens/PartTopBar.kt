package com.bigbingo.brickfinder.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartTopBar(
    titleText: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = titleText,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        expandedHeight = 50.dp,
        colors = TopAppBarColors(
            containerColor = Color(color = 0xff506070),
            scrolledContainerColor = Color.White,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.Black
        )
    )
}