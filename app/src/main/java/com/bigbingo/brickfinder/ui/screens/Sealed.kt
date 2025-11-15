package com.bigbingo.brickfinder.ui.screens

import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartColor

sealed class Screen {
    object Home : Screen()
    object Parts : Screen()
    data class PartsByCategory(val categoryId: Int) : Screen()
    data class PartInfo(val partNum: String) : Screen()
    data class Inventory(
        val part: Part,
        val setNums: List<String>,
        val color: PartColor? = null
    ) : Screen()
    object SetsCatalog : Screen()
    data class SetsTheme(val themeId: Int) : Screen()
    data class SetInfo(val setNum: String, val themeId: Int? = null) : Screen()
}