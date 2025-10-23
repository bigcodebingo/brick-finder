package com.bigbingo.brickfinder.ui.screens.partsbycategory.components

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
import coil.compose.AsyncImage
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.bigbingo.brickfinder.data.Part

@Composable
fun PartCard(part: Part) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(110.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = part.part_img_url,
                contentDescription = part.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                modifier = Modifier.fillMaxHeight(0.55f),
                text = part.part_num,
                fontSize = 7.5.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}