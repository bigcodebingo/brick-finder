package com.bigbingo.brickfinder.ui.screens.partsbycategory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.partsbycategory.components.PartCard
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import com.bigbingo.brickfinder.data.Part
import org.apache.commons.text.FormattableUtils.append


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
    var totalPages by remember { mutableIntStateOf(1) }
    var totalItems by remember { mutableIntStateOf(0) }
    var parts by remember { mutableStateOf(listOf<Part>()) }
    val context = LocalContext.current

    fun loadPage(page: Int) {
        val offset = (page - 1) * pageSize
        val (pageParts, total) = viewModel.fetchPartsPage(categoryId, offset, pageSize, context)
        parts = pageParts
        totalItems = total
        totalPages = (total + pageSize - 1) / pageSize
        currentPage = page
    }

    LaunchedEffect(categoryId) {
        loadPage(1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("$totalItems ") }
                            append("items found. ")
                            append("Page ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){ append("$currentPage") }
                            append(" of ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){ append("$totalPages") }
                            append(" (")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("$pageSize") }
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
                colors = TopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor =Color.Black
                )
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                val maxWindow = 6
                val start = when {
                    totalPages <= maxWindow -> 1
                    currentPage <= totalPages - maxWindow + 1 -> currentPage
                    else -> totalPages - maxWindow + 1
                }
                val end = if (totalPages < maxWindow) totalPages else (start + maxWindow - 1)

                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(25.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable(enabled = currentPage > 1) { loadPage(currentPage - 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Previous",
                        color = if (currentPage > 1) Color(0xFF0788CA) else Color.Black,
                        fontSize = 13.sp,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))


                for (i in start..end) {
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .padding(horizontal = 2.dp)
                            .height(25.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .background(
                                color = if (i == currentPage) Color(0xffdcddd9) else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)

                            )

                            .clickable(enabled = i != currentPage) { loadPage(i) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "$i",
                            color = if (i == currentPage) Color.Black else Color(0xFF0788CA),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(25.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable(enabled = currentPage < totalPages) { loadPage(currentPage + 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Next",
                        color = if (currentPage < totalPages) Color(0xFF0788CA) else Color.Black,
                        fontSize = 13.sp,

                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(parts, key = { it.part_num }) { part ->
                    PartCard(part)
                }
            }
        }
    }
}
