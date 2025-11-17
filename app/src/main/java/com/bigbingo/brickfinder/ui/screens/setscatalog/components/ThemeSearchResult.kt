package com.bigbingo.brickfinder.ui.screens.setscatalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbingo.brickfinder.data.SetTheme

@Composable
fun ThemeSearchResult(
    searchResults: List<Pair<SetTheme, String?>>,
    onResultSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .background(Color.White.copy(alpha = 0.96f), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
            .padding(top = 4.dp, bottom = 4.dp)
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
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                searchResults.forEach { (theme, parentName) ->
                    val displayName = if (parentName != null)
                        "${theme.name} ($parentName)" else theme.name
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 13.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onResultSelected(theme.id) }
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }
            }
        }
    }
}