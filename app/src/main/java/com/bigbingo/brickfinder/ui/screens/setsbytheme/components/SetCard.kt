package com.bigbingo.brickfinder.ui.screens.setsbytheme.components

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
import com.bigbingo.brickfinder.data.Set

@Composable

fun SetCard(
    set: Set,
    backgroundColor: Color,
    onClick: () -> Unit
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
            AsyncImage(
                model = set.set_img_url,
                contentDescription = set.name,
                modifier = Modifier
                    .size(85.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Box(
                modifier = Modifier.fillMaxHeight().width(60.dp),
                contentAlignment = Alignment.TopStart

            ) {
                Text(
                    text = set.set_num.dropLast(2),
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    maxLines = 1,
                    color = Color(0xFF1565C0),
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onClick() }
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),

            ) {
                Text(
                    text = set.name,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${set.num_parts ?: "?"} parts, ${set.year}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
