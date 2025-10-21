package com.bigbingo.brickfinder.ui.screens.sets

import android.R.attr.shape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.ui.screens.sets.components.SetsTopBar
import com.bigbingo.brickfinder.ui.screens.sets.components.TwoColumnScrollableThemeGridWithRightExtra
import com.bigbingo.brickfinder.viewmodel.SetsViewModel
import com.bigbingo.brickfinder.ui.screens.sets.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetsScreen(
    viewModel: SetsViewModel = viewModel(),
    onBack: () -> Unit,
    onParentClick: (String) -> Unit = {},
    onChildClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val themes = remember { viewModel.fetchSetThemes(context) }
    val themeTree = remember(themes) { viewModel.buildThemeTree(themes) }

    var searchQuery by remember { mutableStateOf("") }
    val searchResults = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else viewModel.searchThemes(themes, searchQuery).take(5)
    }

    Scaffold(
        topBar = {
            SetsTopBar(
                titleText = "Find set themes:",
                onBack = onBack
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            SearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (searchQuery.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
                ) {
                    if (searchResults.isEmpty()) {
                        Text(
                            text = "No results",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            searchResults.forEach { (theme, parentName) ->
                                val displayName = if (parentName != null)
                                    "${theme.name} ($parentName)" else theme.name
                                Text(
                                    text = displayName,
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onParentClick(theme.name) }
                                        .padding(vertical = 0.dp, horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
                TwoColumnScrollableThemeGridWithRightExtra(
                    themes = themeTree,
                    onParentClick = onParentClick,
                    onChildClick = onChildClick
                )
            
        }
    }
}
