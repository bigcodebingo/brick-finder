package com.bigbingo.brickfinder.ui.screens.sets.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbingo.brickfinder.viewmodel.ThemeNode

@Composable
fun ThemeList(
    themes: List<ThemeNode>,
    onParentClick: (Int) -> Unit = {},
    onChildClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier

    ) {
    val rightExtra = 31
    val leftCount = (themes.size - rightExtra) / 2
        .coerceAtLeast(0)

    val leftColumn = themes.subList(0, leftCount)
    val rightColumn = themes.subList(leftCount, themes.size)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 8.dp,
                top = 70.dp,
                end = 8.dp,
                bottom = 16.dp),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    leftColumn.forEach { node ->
                        ThemeItem(
                            node = node,
                            onParentClick = onParentClick,
                            onChildClick = onChildClick
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    rightColumn.forEach { node ->
                        ThemeItem(
                            node = node,
                            onParentClick = onParentClick,
                            onChildClick = onChildClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeItem(
    node: ThemeNode,
    onParentClick: (Int) -> Unit = {},
    onChildClick: (Int) -> Unit = {}
    ) {
    Column(modifier = Modifier.padding(start = 6.dp, top = 5.dp)) {
        Text(
            text = node.theme.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(0xff0788CA),
            modifier = Modifier.clickable {
                onParentClick(node.theme.id)
            }
        )
        if (node.children.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(start = 6.dp, top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                node.children.forEach { child ->
                    Text(
                        text = child.theme.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(0xff0788CA),
                        modifier = Modifier.clickable {
                            onChildClick(child.theme.id)
                        }
                    )
                }
            }
        }
    }
}
