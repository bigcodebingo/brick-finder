package com.bigbingo.brickfinder.ui.screens.partinfo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.bigbingo.brickfinder.data.PartColor
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun ColorList(
    partColors: List<PartColor>,
    onClickSets: () -> Unit,
) {
    LazyVerticalGrid(
        contentPadding = PaddingValues(top = 2.dp, bottom = 2.dp),
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(Color(0xffeeeeee))
    ) {
        items(partColors) { color ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                val backColor = try {
                    Color("#${color.rgb}".toColorInt())
                } catch (e: Exception) {
                    Color.Gray
                }

                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .background(backColor)
                )
                ClickableText(
                    text = AnnotatedString("${color.name} (${color.count})"),
                    style = TextStyle(
                        color = Color(0xFF1565C0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline
                    ),
                    onClick = {
                        onClickSets()
                    }
                )
            }
        }
    }
}