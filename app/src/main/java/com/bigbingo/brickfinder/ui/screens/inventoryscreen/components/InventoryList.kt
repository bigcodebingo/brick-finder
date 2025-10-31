package com.bigbingo.brickfinder.ui.screens.inventoryscreen.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.data.PartAppearance
import com.bigbingo.brickfinder.data.Set
import com.bigbingo.brickfinder.ui.screens.setsbytheme.components.ColumnHeader


@Composable
fun InventoryList(
    sets: List<PartAppearance>,
) {
    val groupedByColor = sets.groupBy { it.color }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp),
    ) {
        item {
            InventoryHeader()
        }
        groupedByColor.forEach { (color, appearances) ->

            item {
                InventoryColorHeader(color = color)
            }

            itemsIndexed(appearances) { index, appearance ->
                val backgroundColor = if (index % 2 == 0) Color.White else Color(0xFFF5F5F5)

                Log.d("InventoryList", "PartAppearance: set=${appearance.set.set_num}, color=${appearance.color.name}, qty=${appearance.quantity}")

                InventoryCard(
                    set = appearance,
                    backgroundColor = backgroundColor
                )
            }
        }
    }
}