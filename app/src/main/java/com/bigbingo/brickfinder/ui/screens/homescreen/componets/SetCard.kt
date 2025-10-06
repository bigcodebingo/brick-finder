package com.bigbingo.brickfinder.ui.screens.homescreen.componets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.data.LegoSet

@Composable
fun SetCard(set: LegoSet) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = set.name,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}