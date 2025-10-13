package com.bigbingo.brickfinder.ui.screens.homepage.componets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Search...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        modifier = Modifier.fillMaxWidth()
    )
}