package com.bigbingo.brickfinder.ui.screens.homescreen.componets

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.bigbingo.brickfinder.R

@Composable
fun HomeStaticCards(onNavigate: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(
            modifier = Modifier
                .weight(1f)
                .height(180.dp)
                .clickable { onNavigate(3) },
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, Color.LightGray)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                Image(
                    painter = painterResource(R.drawable.catalog_parts),
                    contentDescription = null,
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Parts", fontSize = 18.sp)
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .height(180.dp)
                .clickable { onNavigate(4) },
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, Color.LightGray)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.catalog_sets),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Sets", fontSize = 18.sp)
            }
        }
    }
}

