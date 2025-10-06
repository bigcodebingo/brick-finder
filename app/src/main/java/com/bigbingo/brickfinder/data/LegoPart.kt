package com.bigbingo.brickfinder.data

import androidx.compose.ui.text.font.FontWeight

data class LegoPart(
    val id: Int,
    val name: String,
    val color: String,
    val imageUrl: String,
    val weight: Int,
    val category: String,
    val years: String
)
