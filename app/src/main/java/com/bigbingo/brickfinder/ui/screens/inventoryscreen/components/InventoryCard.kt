package com.bigbingo.brickfinder.ui.screens.inventoryscreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bigbingo.brickfinder.data.PartAppearance

@Composable

fun InventoryCard(
    set: PartAppearance,
    backgroundColor: Color,
    onSetNumClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxHeight().width(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${set.quantity} in  ",
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    maxLines = 1,
                )
            }
            Box(
                modifier = Modifier.fillMaxHeight().width(50.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = set.set.set_num.removeSuffix("-1"),
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    maxLines = 1,
                    textDecoration = TextDecoration.Underline,
                    color = Color(0xFF1565C0),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { onSetNumClick(set.set.set_num) }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()

                ) {
                Text(
                    text = set.set.name,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${set.set.num_parts ?: "?"} parts, ${set.set.year}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            AsyncImage(
                model = set.set.set_img_url,
                contentDescription = set.set.name,
                modifier = Modifier
                    .size(85.dp)
            )
        }
    }
}