package com.bigbingo.brickfinder.ui.screens.setinfo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bigbingo.brickfinder.data.SetInventory
import com.bigbingo.brickfinder.viewmodel.PartsViewModel

@Composable
fun SetPartsList(
    currentInventory: SetInventory,
    setImageUrl: String?,
    year: Int?,
    numParts: Int?,
    inventories: List<SetInventory>,
    onInventoryClick: (SetInventory) -> Unit,
    onPartNumClick: (String) -> Unit,
    partViewModel: PartsViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(top = 6.dp, bottom = 6.dp)
    ) {

        item(span = { GridItemSpan(5) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                ) {
                    AsyncImage(
                        model = setImageUrl,
                        contentDescription = "Set Image",
                        modifier = Modifier
                            .size(200.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text(
                            text = "Year: ${year ?: "—"}",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 13.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Parts: ${numParts ?: "—"}",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 13.sp)
                        )
                        if (inventories.size > 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Inventory v.${currentInventory.version}",
                                color = Color(0xFF1565C0),
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier
                                    .clickable {
                                        val currentIndex =
                                            inventories.indexOf(currentInventory)
                                        val nextIndex =
                                            (currentIndex + 1) % inventories.size
                                        onInventoryClick(inventories[nextIndex])
                                    },
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }
            }
        }
        item(span = { GridItemSpan(5) }) {
            Spacer(modifier = Modifier.height(6.dp))
        }

        if (currentInventory.parts.isNotEmpty()) {
            item(span = { GridItemSpan(5) }) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFc0c0c0))
                        .fillMaxWidth()
                        .height(25.dp)
                ) {
                    Text(
                        text = "Parts (total ${currentInventory.parts.sumOf { it.third }})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
            items(currentInventory.parts) { (partNum, imgUrl, qty) ->
                SetItem(
                    imgUrl = imgUrl,
                    num = partNum,
                    qty = qty,
                    clickable = true,
                    underline = true,
                    textColor = Color(0xFF1565C0),
                    onClick = {
                        partViewModel.clearPart()
                        partViewModel.clearParts()
                        onPartNumClick(partNum)
                    }
                )
            }
        }

        if (currentInventory.minifigs.isNotEmpty()) {
            item(span = { GridItemSpan(5) }) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFc0c0c0))
                        .fillMaxWidth()
                        .height(25.dp),

                    ) {
                    Text(
                        text = "Minifigures (total ${currentInventory.minifigs.sumOf { it.third }})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
            items(currentInventory.minifigs) { (figNum, imgUrl, qty) ->
                SetItem(
                    imgUrl = imgUrl,
                    num = figNum,
                    qty = qty
                )
            }
        }

        if (currentInventory.minifigParts.isNotEmpty()) {
            item(span = { GridItemSpan(5) }) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFc0c0c0))
                        .fillMaxWidth()
                        .height(25.dp),

                    ) {
                    Text(
                        text = "Minifig parts (total ${currentInventory.minifigParts.sumOf { it.third }})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
            items(currentInventory.minifigParts) { (partNum, imgUrl, qty) ->
                SetItem(
                    imgUrl = imgUrl,
                    num = partNum,
                    qty = qty,
                    clickable = true,
                    underline = true,
                    textColor = Color(0xFF1565C0),
                    onClick = {
                        partViewModel.clearPart()
                        partViewModel.clearParts()
                        onPartNumClick(partNum)
                    }
                )
            }
        }
    }
}