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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.data.ItemType
import com.bigbingo.brickfinder.data.Item
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbingo.brickfinder.helpers.DatabaseHelper
import com.bigbingo.brickfinder.helpers.DatabaseHelper.getThemeIdBySetNum
import com.bigbingo.brickfinder.ui.screens.LoadingScreen
import com.bigbingo.brickfinder.ui.screens.setinfo.components.SetTopBar
import com.bigbingo.brickfinder.viewmodel.PartsViewModel
import com.bigbingo.brickfinder.viewmodel.SetsViewModel
import com.bigbingo.brickfinder.ui.screens.setinfo.components.SetPartsList
import com.bigbingo.brickfinder.viewmodel.WantedListViewModel


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
    wantedListViewModel: WantedListViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val name by viewModel.setName.collectAsState()
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

    var isInWantedList by remember { mutableStateOf(false) }

    LaunchedEffect(setNum) {
        viewModel.loadSetInfo(context, setNum)
        isInWantedList = wantedListViewModel.isInWantedList(context, setNum, ItemType.SET)
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
                    viewModel = viewModel,
                    isInWantedList = isInWantedList,
                    onWantedListToggle = {
                        if (isInWantedList) {
                            wantedListViewModel.removeFromWantedList(context, setNum, ItemType.SET)
                            isInWantedList = false
                        } else {
                            val wantedItem = Item(
                                itemNum = setNum,
                                name = name,
                                type = ItemType.SET,
                                imageUrl = setImageUrl,
                                year = year,
                                numParts = numParts
                            )
                            wantedListViewModel.addToWantedList(context, wantedItem)
                            isInWantedList = true
                        }
                    }
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
