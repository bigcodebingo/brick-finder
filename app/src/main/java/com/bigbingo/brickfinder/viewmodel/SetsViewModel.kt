package com.bigbingo.brickfinder.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.bigbingo.brickfinder.data.SetTheme
import com.bigbingo.brickfinder.data.db.DatabaseHelper

data class ThemeNode(
    val theme: SetTheme,
    val children: List<ThemeNode> = emptyList()
)

class SetsViewModel : ViewModel() {

    fun fetchSetThemes(context: Context): List<SetTheme> {
        return DatabaseHelper.getAllSetThemes(context)
    }

    fun buildThemeTree(themes: List<SetTheme>): List<ThemeNode> {
        val childrenMap = themes.groupBy { it.parent_id }
        return themes
            .filter { it.parent_id == null }
            .map { theme ->
                ThemeNode(theme, buildChildren(theme.id, childrenMap))
            }
    }

    private fun buildChildren(parentId: Int, childrenMap: Map<Int?, List<SetTheme>>): List<ThemeNode> {
        return childrenMap[parentId]
            ?.map { child -> ThemeNode(child, buildChildren(child.id, childrenMap)) }
            ?: emptyList()
    }
}