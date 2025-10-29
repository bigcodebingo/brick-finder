package com.bigbingo.brickfinder.ui.screens.partinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.ui.screens.LoadingScreen
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import com.bigbingo.brickfinder.data.db.DatabaseHelper
import com.bigbingo.brickfinder.ui.screens.partinfo.components.ColorList
import com.bigbingo.brickfinder.ui.screens.PartTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartInfoScreen(
    partNum: String,
    onBack: () -> Unit,
    onClickSets: (Part) -> Unit,
    viewModel: PartsViewModel = viewModel(),
) {
    val context = LocalContext.current
    val part by viewModel.part.collectAsState()
    val setNums  by viewModel.setNums.collectAsState()
    val yearRange by viewModel.yearRange.collectAsState()
    val partColors   by viewModel.partColors.collectAsState()
    val categoryName = remember(part) {
        part?.let { p ->
            DatabaseHelper.getPartCategories(context)
                .find { it.id == p.part_cat_id }?.name
        }
    }

    val isFirstLoad = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (part?.part_num != partNum) {
            viewModel.loadPartFromDb(context, partNum)
            viewModel.loadPartInfo(context, partNum)
        }
    }
    LaunchedEffect(part) {
        if (part != null && categoryName != "null") {
            isFirstLoad.value = false
        }
    }



    if (isFirstLoad.value && part == null) {
        LoadingScreen()
    }
    else{
        Scaffold(
            topBar = {
                PartTopBar(
                    titleText = "$categoryName: ${part?.part_num ?: partNum}",
                    onBack = {
                        viewModel.clearPart()
                        onBack()
                    }
                )
            },
            containerColor = Color.White
        ) { innerPadding ->
            part?.let { p ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(start = 10.dp, top = 15.dp, end = 10.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = p.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("Item No: ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF2C6EA5)))
                            { append(p.part_num) } },
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 13.sp),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    AsyncImage(
                        model = p.part_img_url,
                        contentDescription = p.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.35f)
                            .border(
                                width = 1.dp,
                                color = Color(0xFFCCCCCC),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(all = 5.dp),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("Years released: ")
                            val (minYear, maxYear) = yearRange
                            if (minYear != null && maxYear != null) {
                                if (minYear == maxYear) {
                                    append("$minYear")
                                } else {
                                    append("$minYear - $maxYear")
                                }
                            } else {
                                append("N/A")
                            }
                        },
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 13.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val count = setNums.size
                    val plural = if (count == 1) "set" else "sets"
                    val annotatedText = buildAnnotatedString {
                        append("Item appears in: ")
                        pushStringAnnotation(tag = "SETS", annotation = "sets_click")
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF1565C0),
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("$count $plural")
                        }
                        pop()
                    }
                    ClickableText(
                        text = annotatedText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 13.sp,
                            color = Color.Black
                        ),
                        onClick = { offset ->
                            annotatedText.getStringAnnotations(
                                tag = "SETS",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                onClickSets(p)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .background(color = Color(0xff506070))
                            .fillMaxWidth()
                            .padding(all=6.dp)
                    ) {
                        Text(
                            text = "By Color:",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp)
                        )
                    }
                    if (partColors.size>1) {
                        /*ColorList(partColors, onClickSets = onClickSets)*/
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                        ) {
                            Text(
                                text = "No colors available",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

