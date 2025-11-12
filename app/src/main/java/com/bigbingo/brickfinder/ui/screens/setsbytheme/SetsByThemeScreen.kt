package com.bigbingo.brickfinder.ui.screens.setsbytheme

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.LoadingScreen
import com.bigbingo.brickfinder.ui.screens.setsbytheme.components.ThemeTopBar
import com.bigbingo.brickfinder.ui.screens.PaginationBar
import com.bigbingo.brickfinder.ui.screens.setsbytheme.components.SetsList
import com.bigbingo.brickfinder.viewmodel.SetsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetsThemeScreen(
    themeId: Int,
    onBack: () -> Unit,
    viewModel: SetsViewModel = viewModel(),
    onSetClick: (String) -> Unit
) {
    val pageSize = 25
    val currentPage by viewModel.currentPage.collectAsState()
    val sets by viewModel.sets.collectAsState()
    val totalSets by viewModel.totalSets.collectAsState()
    val totalPages = (totalSets + pageSize - 1) / pageSize

    val isFirstLoad = remember { mutableStateOf(true) }
    var themeName by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val context = LocalContext.current

    LaunchedEffect(currentPage) {
        viewModel.fetchSetsPage(themeId, currentPage, pageSize, context)
    }

    LaunchedEffect(Unit) {
        if (sets.isEmpty()) {
            viewModel.fetchSetsPage(themeId, currentPage, pageSize, context)
        }
        themeName = viewModel.getThemeNameById(context, themeId)
    }

    LaunchedEffect(sets) {
        if (sets.isNotEmpty()) {
            isFirstLoad.value = false
        }
    }

    if (isFirstLoad.value && sets.isEmpty()) {
        LoadingScreen()
    } else {
        Scaffold(
            topBar = {
                ThemeTopBar(

                    titleText = buildAnnotatedString {
                        val plural = if (totalSets == 1) "set" else "sets"
                        append(themeName)
                        append(" ")
                        withStyle(style = SpanStyle(fontSize = 13.sp, color = Color.LightGray)) {
                            append("($totalSets $plural)")
                        }
                    },
                    onBack = onBack
                )
            },
            containerColor = Color(0xffeeeeee)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                SetsList(
                    sets = sets,
                    listState = listState,
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onSetClick = onSetClick,
                    onPageChange = { page -> viewModel.setCurrentPage(page) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 95.dp)
                )

                if (sets.size < 9 && sets.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    PaginationBar(
                        currentPage = currentPage,
                        totalPages = totalPages,
                        onPageChange = { page -> viewModel.setCurrentPage(page) },
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
            }
        }
    }
}