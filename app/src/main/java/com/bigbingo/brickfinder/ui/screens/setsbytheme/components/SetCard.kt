package com.bigbingo.brickfinder.ui.screens.setsbytheme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bigbingo.brickfinder.data.Set


@Composable
fun SetCard(set: Set) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(140.dp)
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = set.set_img_url,
                contentDescription = set.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = set.name,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${set.num_parts ?: "?"} parts, ${set.year}",
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}
