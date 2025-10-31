package com.bigbingo.brickfinder.ui.screens.inventoryscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.bigbingo.brickfinder.data.PartColor

@Composable
fun InventoryHeader() {
    Box(
        modifier = Modifier
            .background(Color(0xFF5e5a80))
            .fillMaxWidth()
            .height(25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxHeight().width(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Qty  ",
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
            Box(
                modifier = Modifier.fillMaxHeight().width(50.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Item No",
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .width(130.dp).fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Description ",
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(85.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Image",
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
        }
    }
}