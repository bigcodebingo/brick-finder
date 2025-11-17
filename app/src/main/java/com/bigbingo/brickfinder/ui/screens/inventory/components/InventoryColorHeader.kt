package com.bigbingo.brickfinder.ui.screens.inventory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.bigbingo.brickfinder.data.PartColor

@Composable
fun InventoryColorHeader(color: PartColor) {
    val backColor = Color("#${color.rgb}".toColorInt())
    Box(
        modifier = Modifier
            .background(Color(0xFFc0c0c0))
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
                modifier = Modifier
                    .size(15.dp)
                    .background(backColor)
            )
            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = color.name   ,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
            )
        }
    }
}