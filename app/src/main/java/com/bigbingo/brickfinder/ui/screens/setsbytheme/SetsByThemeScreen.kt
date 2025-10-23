package com.bigbingo.brickfinder.ui.screens.setsbytheme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.bigbingo.brickfinder.ui.screens.setsbytheme.components.SetCard
import com.bigbingo.brickfinder.ui.screens.setsbytheme.components.ThemeTopBar
import com.bigbingo.brickfinder.ui.screens.PaginationBar
import com.bigbingo.brickfinder.ui.screens.setsbytheme.components.ColumnHeader
import com.bigbingo.brickfinder.viewmodel.SetsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetsThemeScreen(
    themeId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SetsViewModel = viewModel()
) {
    val listState = rememberLazyListState()
    val pageSize = 25
    var currentPage by remember { mutableIntStateOf(1) }
    val sets by viewModel.sets.collectAsState()
    val totalSets by viewModel.totalSets.collectAsState()
    val totalPages = (totalSets + pageSize - 1) / pageSize
    val context = LocalContext.current

    var totalSetsStable by remember { mutableStateOf(0) }
    var themeName by remember { mutableStateOf("") }


    fun loadPage(page: Int) {
        viewModel.clearSets()
        val offset = (page - 1) * pageSize
        viewModel.fetchSetsPage(context, themeId, offset, pageSize)
        currentPage = page
    }

    LaunchedEffect(totalSets) {
        if (totalSets > 0 && totalSetsStable == 0) {
            totalSetsStable = totalSets
        }
    }

    LaunchedEffect(currentPage) {
        listState.scrollToItem(0)
    }

    LaunchedEffect(themeId) {
        loadPage(1)
        themeName = viewModel.getThemeNameById(context, themeId)
    }

    Scaffold(
        topBar = {
            ThemeTopBar(
                titleText = buildAnnotatedString {
                    append(themeName)
                    append(" ")
                    withStyle(style = SpanStyle(fontSize = 14.sp, color = Color.Gray)) {
                        append("($totalSetsStable sets)")
                    }
                },
                onBack = onBack
            )
        },
        bottomBar = {
            if (sets.size<9 && sets.isNotEmpty()) {
                PaginationBar(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPageChange = { page -> loadPage(page) },
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        },
        modifier = modifier,
        containerColor = Color(0xffeeeeee)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f) .padding(top=6.dp)
            ) {
                item{
                    ColumnHeader()
                }
                itemsIndexed(sets) { index, set ->
                    val backgroundColor =
                        if (index % 2 == 0) Color.White else Color(color = 0xfff1f5f8)

                    SetCard(set = set, backgroundColor = backgroundColor)
                }

                if (sets.size>=9) {
                    item {
                        PaginationBar(
                            currentPage = currentPage,
                            totalPages = totalPages,
                            onPageChange = { page -> loadPage(page) },
                            modifier = Modifier.padding(top = 16.dp,bottom = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
