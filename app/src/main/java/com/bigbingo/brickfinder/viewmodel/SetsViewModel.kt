package com.bigbingo.brickfinder.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbingo.brickfinder.data.SetTheme
import com.bigbingo.brickfinder.data.db.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.bigbingo.brickfinder.data.Set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun clearSets() {
        _sets.value = emptyList()
        _totalSets.value = 0
    }

    fun getThemeNameById(context: Context, themeId: Int): String {
        val themes = fetchSetThemes(context)
        return themes.find { it.id == themeId }?.name ?: "Unknown Theme"
    }

    fun fetchSetsPage(context: Context, themeId: Int?, offset: Int, limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val (setsList, total) = DatabaseHelper.getSetsPage(context, themeId, offset, limit)
            withContext(Dispatchers.Main) {
                _sets.value = setsList
                _totalSets.value = total
            }
            _isLoading.value = false
        }
    }

    fun searchThemes(themes: List<SetTheme>, query: String): List<Pair<SetTheme, String?>> {
        return themes.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.id.toString().contains(query)
        }.map { theme ->
            val parentName = themes.find { it.id == theme.parent_id }?.name
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