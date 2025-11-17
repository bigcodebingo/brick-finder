package com.bigbingo.brickfinder.ui.screens.home.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun HomeCard(
    title: String,
    countText: String,
    imageRes: Int,
    backgroundColor: Color,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .width(155.dp)
            .height(185.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color = colors.onPrimary,
                        lineHeight = 16.sp
                    )
                    Text(
                        text = countText,
                        fontSize = 13.sp,
                        color = Color(0xff999999),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

