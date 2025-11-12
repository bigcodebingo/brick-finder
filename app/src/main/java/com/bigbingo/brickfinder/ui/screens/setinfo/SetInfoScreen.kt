package com.bigbingo.brickfinder.ui.screens.setinfo

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.data.db.DatabaseHelper
import com.bigbingo.brickfinder.data.db.DatabaseHelper.getThemeIdBySetNum
import com.bigbingo.brickfinder.ui.screens.LoadingScreen
import com.bigbingo.brickfinder.ui.screens.setinfo.components.SetTopBar
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import com.bigbingo.brickfinder.viewmodel.SetsViewModel
import com.bigbingo.brickfinder.ui.screens.setinfo.components.SetPartsList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetInfoScreen(
    setNum: String,
    themeId: Int? = null,
    onBack: () -> Unit,
    onCatalogClick: () -> Unit = {},
    onSetsClick: () -> Unit = {},
    onThemeClick: (Int) -> Unit,
    onPartNumClick: (String) -> Unit,
    viewModel: SetsViewModel = viewModel(),
    partViewModel: PartsViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val year by viewModel.setYear.collectAsState()
    val numParts by viewModel.setNumParts.collectAsState()
    val selectedInventory by viewModel.selectedInventory.collectAsState()

    val inventories = viewModel.setInventories.collectAsState().value
    val setImageUrl = viewModel.setImageUrl.collectAsState().value

    val resolvedThemeId = remember(setNum, themeId) {
        themeId ?: getThemeIdBySetNum(context, setNum)
    }

    val themeName = remember(resolvedThemeId) {
        resolvedThemeId?.let { id ->
            DatabaseHelper.getAllSetThemes(context).find { it.id == id }?.name
        }
    }

    LaunchedEffect(setNum) {
        viewModel.loadSetInfo(context, setNum)
    }

    val currentInventory = selectedInventory

    if ((year == null && numParts == null) || currentInventory == null) {
        LoadingScreen()
    } else {
        Scaffold(
            topBar = {
                SetTopBar(
                    setNum = setNum,
                    themeName = themeName,
                    onBack = {
                        viewModel.clearSetInfo()
                        onBack()
                    },
                    onCatalogClick = onCatalogClick,
                    onSetsClick = onSetsClick,
                    onThemeClick = {
                        resolvedThemeId?.let { id ->
                            onThemeClick(id)
                        }
                    },
                    viewModel = viewModel
                )
            },
            containerColor = Color(0xffeeeeee)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .width(340.dp)
                ) {
                    SetPartsList(
                        currentInventory = currentInventory,
                        setImageUrl = setImageUrl,
                        year = year,
                        numParts = numParts,
                        inventories = inventories,
                        onInventoryClick = { nextInventory ->
                            val index = inventories.indexOf(nextInventory)
                            viewModel.selectInventory(index)
                        },
                        onPartNumClick = onPartNumClick,
                        partViewModel = partViewModel
                    )
                }
            }
        }
    }
}
