package com.bigbingo.brickfinder.ui.screens.partinfo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.data.Item
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.ItemType
import com.bigbingo.brickfinder.data.PartColor
import com.bigbingo.brickfinder.ui.screens.LoadingScreen
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import com.bigbingo.brickfinder.helpers.DatabaseHelper
import com.bigbingo.brickfinder.ui.screens.partinfo.components.ColorList
import com.bigbingo.brickfinder.ui.screens.PartTopBar
import com.bigbingo.brickfinder.viewmodel.WantedListViewModel
import com.bigbingo.brickfinder.ui.filament.FilamentAssets
import com.bigbingo.brickfinder.ui.filament.FilamentModelView
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartInfoScreen(
    partNum: String,
    onBack: () -> Unit,
    onCatalogClick: () -> Unit,
    onPartsClick: () -> Unit,
    onCategoryClick: (Int) -> Unit,
    onPartNumClick: () -> Unit,
    onSetsClick: (Part, List<String>) -> Unit,
    onColorClick: (Part, List<String>, PartColor) -> Unit,

    viewModel: PartsViewModel = viewModel(),
    wantedListViewModel: WantedListViewModel = viewModel(),

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

    val modelAssetPath = remember(part?.part_num) {
        part?.part_num?.let { FilamentAssets.findModelAsset(context.assets, it) }
    }
    var modelFailed by remember(part?.part_num) { mutableStateOf(false) }
    var showModel by remember(part?.part_num) { mutableStateOf(false) }

    var isInWantedList by remember { mutableStateOf(false) }

    LaunchedEffect(part?.part_num) {
        part?.part_num?.let { partNum ->
            isInWantedList = wantedListViewModel.isInWantedList(context, partNum, ItemType.PART)
        } ?: run { isInWantedList = false }
    }
    LaunchedEffect(part?.part_num) {
        modelFailed = false
        showModel = false
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
                    partNum = part?.part_num,
                    categoryName = categoryName,
                    onBack = {
                        viewModel.clearPart()
                        viewModel.clearPartAppearances()
                        onBack()
                    },
                    onCatalogClick = onCatalogClick,
                    onPartsClick = onPartsClick,

                    onCategoryClick = {
                        part?.part_cat_id?.let { id ->
                            Log.d("PartTopBarDebug", "onCategoryClick called with categoryId = $id, partNum = ${part!!.part_num}")
                            onCategoryClick(id)
                        } ?: Log.d("PartTopBarDebug", "onCategoryClick: part or part_cat_id is null")
                    },
                    onPartNumClick = onPartNumClick,
                    viewModel = viewModel,
                    showInSets = false,
                    isInWantedList = isInWantedList,
                    onWantedListToggle = {
                        part?.let { p ->
                            if (isInWantedList) {
                                wantedListViewModel.removeFromWantedList(context, p.part_num, ItemType.PART)
                                isInWantedList = false
                            } else {
                                val wantedItem = Item(
                                    itemNum = p.part_num,
                                    name = p.name,
                                    type = ItemType.PART,
                                    imageUrl = p.part_img_url
                                )
                                wantedListViewModel.addToWantedList(context, wantedItem)
                                isInWantedList = true
                            }
                        }
                    }
                )
            },
            containerColor = Color.White
        ) { innerPadding ->
            part?.let { p ->
                val isDefaultImage = p.part_img_url ==
                        "https://cdn.rebrickable.com/media/thumbs/nil.png/85x85p.png?1662040927.7130826"

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = buildAnnotatedString {
                                append("Item No: ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF2C6EA5)))
                                { append(p.part_num) } },
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 13.sp),
                            lineHeight = 18.sp
                        )
                        if (modelAssetPath != null) {
                            Spacer(modifier = Modifier.width(12.dp))
                            val label = if (showModel && !modelFailed) "3d model" else "image"
                            Text(
                                text = label,
                                color = Color(0xFF2C6EA5),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                ),
                                modifier = Modifier.clickable {
                                    if (!modelFailed) {
                                        showModel = !showModel
                                    }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.35f)
                            .border(
                                width = 1.dp,
                                color = Color(0xFFCCCCCC),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val show3D = modelAssetPath != null && !modelFailed
                        if (show3D && showModel) {
                            modelAssetPath.let { path ->
                                FilamentModelView(
                                    modelPath = path,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    onLoadFailed = { modelFailed = true }
                                )
                            }
                        } else {
                            AsyncImage(
                                model = p.part_img_url,
                                contentDescription = p.name,
                                modifier = Modifier
                                    .size(if (isDefaultImage) 80.dp else 200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
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
                    val clickableModifier = if (count > 0) {
                        Modifier.clickable { onSetsClick(p, setNums) }
                    } else { Modifier }
                    Row {
                        Text(
                            text = "Item appears in: ",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 13.sp,
                                color = Color.Black))
                        Text(
                            text = "$count $plural",
                            color = if (count > 0) Color(0xFF1565C0) else Color.Gray,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 13.sp,
                                textDecoration = if (count > 0) TextDecoration.Underline else TextDecoration.None
                            ),
                            modifier = clickableModifier
                        )
                    }

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
                        ColorList(partColors) { color ->
                            onColorClick(p, setNums, color)
                        }
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

