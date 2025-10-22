package com.bigbingo.brickfinder.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.SetTheme
import com.bigbingo.brickfinder.data.db.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.bigbingo.brickfinder.data.Set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ThemeNode(
    val theme: SetTheme,
    val children: List<ThemeNode> = emptyList()
)

class SetsViewModel : ViewModel() {

    private val _sets = MutableStateFlow<List<Set>>(emptyList())
    val sets: StateFlow<List<Set>> = _sets

    private val _totalSets = MutableStateFlow(0)
    val totalSets: StateFlow<Int> = _totalSets

    private val _selectedThemeId = MutableStateFlow<Int?>(null)
    val selectedThemeId: StateFlow<Int?> = _selectedThemeId

    fun clearSets() {
        _sets.value = emptyList()
        _totalSets.value = 0
    }

    fun fetchSetsPage(context: Context, themeId: Int?, offset: Int, limit: Int) {
        _selectedThemeId.value = themeId
        viewModelScope.launch(Dispatchers.IO) {
            val (setsList, total) = DatabaseHelper.getSetsPage(context, themeId, offset, limit)
            withContext(Dispatchers.Main) {
                _sets.value = setsList
                _totalSets.value = total
            }
        }
    }

    fun searchThemes(themes: List<SetTheme>, query: String): List<Pair<SetTheme, String?>> {
        return themes.filter { it.name.contains(query, ignoreCase = true)
        }.map { theme -> val parentName = themes.find { it.id == theme.parent_id }?.name
            theme to parentName
        }
    }

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