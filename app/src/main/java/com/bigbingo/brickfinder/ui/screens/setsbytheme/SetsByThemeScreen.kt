package com.bigbingo.brickfinder.ui.screens.setsbytheme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bigbingo.brickfinder.ui.screens.setsbytheme.components.ThemeTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetsThemeScreen(
    onBack: () -> Unit,
    themeName: String
) {
    Scaffold(
        topBar = {
            ThemeTopBar(
                titleText = themeName,
                onBack = onBack
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        }
    }
}