package com.bigbingo.brickfinder.ui.screens.homepage.componets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager: FocusManager = LocalFocusManager.current

    var textFieldValue by remember(value) {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }

    LaunchedEffect(value) {
        if (textFieldValue.text != value) {
            textFieldValue = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        }
    }

    val displayPlaceholder = value.isEmpty()
    val placeholderColor = if (isFocused) Color.Gray.copy(alpha = 0.4f) else Color.Gray.copy(alpha = 0.6f)
    val textColor = if (value.isEmpty()) Color.Transparent else Color.Black

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onValueChange(newValue.text)
        },
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        ),
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isFocused) Color(0xff2678c0) else Color.Gray.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp)
            ),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                focusManager.clearFocus()
            },
            onDone = {
                onSearch()
                focusManager.clearFocus()
            }
        ),
        placeholder = {
            if (displayPlaceholder) {
                Text(
                    text = "Search...",
                    color = placeholderColor,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color.Black
            )
        },
        trailingIcon = {
            when {
                value.isNotEmpty() -> {
                    IconButton(onClick = {
                        onValueChange("")
                        textFieldValue = TextFieldValue("", TextRange(0))
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
                isFocused -> {
                    Text(
                        text = "Cancel",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable {
                                focusManager.clearFocus()
                                onValueChange("")
                                textFieldValue = TextFieldValue("", TextRange(0))
                            }
                            .padding(end = 15.dp)
                    )
                }
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        interactionSource = interactionSource
    )
}