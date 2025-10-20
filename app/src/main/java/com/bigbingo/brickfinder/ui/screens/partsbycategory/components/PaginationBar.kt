package com.bigbingo.brickfinder.ui.screens.partsbycategory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    val maxWindow = 6
    val start = when {
        totalPages <= maxWindow -> 1
        currentPage <= totalPages - maxWindow + 1 -> currentPage
        else -> totalPages - maxWindow + 1
    }
    val end = if (totalPages < maxWindow) totalPages else (start + maxWindow - 1)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(25.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .background(Color.White, RoundedCornerShape(4.dp))
                .clickable(enabled = currentPage > 1) { onPageChange(currentPage - 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Previous",
                color = if (currentPage > 1) Color(0xFF0788CA) else Color.Black,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.width(8.dp))

        for (i in start..end) {
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .padding(horizontal = 2.dp)
                    .height(25.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                    .background(
                        if (i == currentPage) Color(0xffdcddd9) else Color.Transparent,
                        RoundedCornerShape(4.dp)
                    )
                    .clickable(enabled = i != currentPage) { onPageChange(i) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$i",
                    color = if (i == currentPage) Color.Black else Color(0xFF0788CA),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .width(70.dp)
                .height(25.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .background(Color.White, RoundedCornerShape(4.dp))
                .clickable(enabled = currentPage < totalPages) { onPageChange(currentPage + 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Next",
                color = if (currentPage < totalPages) Color(0xFF0788CA) else Color.Black,
                fontSize = 13.sp
            )
        }
    }
}