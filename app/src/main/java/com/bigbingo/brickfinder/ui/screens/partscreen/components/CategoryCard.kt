package com.bigbingo.brickfinder.ui.screens.partscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbingo.brickfinder.data.PartCategory
import coil.compose.AsyncImage
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign


@Composable
fun CategoryCard(category: PartCategory) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(175.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                modifier = Modifier.fillMaxHeight(
                    0.6f
                ),
                text = category.name + " (${category.part_count})",
                fontSize = 9.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )
        }
    }
}

