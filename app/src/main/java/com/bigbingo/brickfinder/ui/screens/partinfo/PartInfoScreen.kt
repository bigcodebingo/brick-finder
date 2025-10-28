package com.bigbingo.brickfinder.ui.screens.partinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bigbingo.brickfinder.ui.screens.LoadingScreen
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import com.bigbingo.brickfinder.data.db.DatabaseHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartInfoScreen(
    partNum: String,
    onBack: () -> Unit,
    viewModel: PartsViewModel = viewModel(),
) {
    val context = LocalContext.current
    val part by viewModel.part.collectAsState()
    val categoryName = remember(part) {
        part?.let { p ->
            DatabaseHelper.getPartCategories(context)
                .find { it.id == p.part_cat_id }?.name
        }
    }
    LaunchedEffect(partNum) {
        viewModel.loadPartFromDb(context, partNum)
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.clearPart() }
    }

    if (part == null || categoryName == "null") {

        return
    }
    else{
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "$categoryName: ${part?.part_num ?: partNum}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.clearPart()
                            onBack()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    expandedHeight = 50.dp,
                    colors = TopAppBarColors(
                        containerColor = Color(color = 0xff506070),
                        scrolledContainerColor = Color.White,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.Black ) )
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
                    Image(
                        painter = rememberAsyncImagePainter(p.part_img_url),
                        contentDescription = "Part image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(all = 5.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFFCCCCCC),
                                shape = RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Item Appears In: ",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 13.sp)
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
                    /*LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .padding(bottom = 8.dp)
                            .background(Color(0xFFEEEEEE)),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(colors) { color ->
                            Box(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "${color.color_name} (${color.num_sets})",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                                    color = Color.Black
                                )
                            }
                        }
                    }*/
                }
            }
        }
    }
}

