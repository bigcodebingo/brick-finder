package com.bigbingo.brickfinder.ui.screens.home.componets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bigbingo.brickfinder.data.ItemType
import com.bigbingo.brickfinder.data.SearchItem

@Composable
fun HomeSearchResult(
    searchResults: List<SearchItem>,
    onResultSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .border(width = 1.dp, color = Color.Gray, shape = MaterialTheme.shapes.medium)
            .padding(vertical = 4.dp)
            .clickable(
                enabled = false,
                onClick = {},
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        if (searchResults.isEmpty()) {
            Text(
                text = "No results",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                searchResults.forEachIndexed { index, item ->
                    if (index == 0) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onResultSelected(item.itemNum) }
                                .padding(vertical = 8.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {

                            Image(
                                painter = rememberAsyncImagePainter(item.imageUrl),
                                contentDescription = item.name,
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(Color.White)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = when(item.type) {
                                        ItemType.SET -> "[Set] ${item.name}"
                                        ItemType.PART -> "[Part] ${item.name}"
                                    },
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Item #${item.itemNum}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(
                                        when(item.type) {
                                            ItemType.SET -> "[Set] "
                                            ItemType.PART -> "[Part] "
                                        }
                                    )
                                }
                                append(item.name)
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 13.sp,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onResultSelected(item.itemNum) }
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}