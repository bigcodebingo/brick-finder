package com.bigbingo.brickfinder.ui.screens.setsbytheme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun ColumnHeader() {
    Box(
        modifier = Modifier
            .background(Color(0xFFc8d3db))
            .fillMaxWidth()
            .height(30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Image",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.width(85.dp)
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = "Item No.",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                maxLines = 1,
                modifier = Modifier.width(60.dp)
            )

            Spacer(modifier = Modifier.width(30.dp))

            Text(
                text = "Description",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}