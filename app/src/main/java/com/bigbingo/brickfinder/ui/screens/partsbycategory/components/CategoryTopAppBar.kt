package com.bigbingo.brickfinder.ui.screens.partsbycategory.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTopAppBar(
    totalItems: Int,
    currentPage: Int,
    totalPages: Int,
    pageSize: Int,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("$totalItems ") }
                    append("items found. Page ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("$currentPage") }
                    append(" of ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("$totalPages") }
                    append(" (")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("$pageSize") }
                    append(" items per page)")
                },
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 11.sp)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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