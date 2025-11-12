package com.bigbingo.brickfinder.ui.screens.setinfo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SetItem(
    imgUrl: String?,
    num: String,
    qty: Int,
    modifier: Modifier = Modifier,
    clickable: Boolean = false,
    onClick: (() -> Unit)? = null,
    textColor: Color = Color.Unspecified,
    underline: Boolean = false
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .size(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imgUrl,
            contentDescription = num,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = num,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textDecoration = if (underline) TextDecoration.Underline else TextDecoration.None,
            modifier = if (clickable && onClick != null) Modifier.clickable { onClick() } else Modifier
        )
        Text(
            text = "x$qty",
            style = MaterialTheme.typography.bodySmall
        )
    }
}