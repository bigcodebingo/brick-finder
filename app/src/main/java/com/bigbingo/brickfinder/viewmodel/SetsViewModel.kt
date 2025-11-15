package com.bigbingo.brickfinder.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.SetTheme
import com.bigbingo.brickfinder.helpers.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.bigbingo.brickfinder.data.Set
import com.bigbingo.brickfinder.data.SetInventory
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
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage
    private val _setName = MutableStateFlow<String?>(null)
    val setName: StateFlow<String?> = _setName
    private val _setYear = MutableStateFlow<Int?>(null)
    val setYear: StateFlow<Int?> = _setYear
    private val _setNumParts = MutableStateFlow<Int?>(null)
    val setNumParts: StateFlow<Int?> = _setNumParts
    private val _setInventories = MutableStateFlow<List<SetInventory>>(emptyList())
    val setInventories: StateFlow<List<SetInventory>> = _setInventories

    private val _selectedInventory = MutableStateFlow<SetInventory?>(null)
    val selectedInventory: StateFlow<SetInventory?> = _selectedInventory
    private val _setImageUrl = MutableStateFlow<String?>(null)
    val setImageUrl: StateFlow<String?> = _setImageUrl

    fun clearSetInfo(){
        _setYear.value = null
        _setNumParts.value = null
        _setInventories.value = emptyList()
        _selectedInventory.value = null
    }
    fun clearSets() {
        _sets.value = emptyList()
    }

    fun resetCurrentPage() {
        _currentPage.value = 1
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
        _sets.value = emptyList()
    }
    fun getThemeNameById(context: Context, themeId: Int): String {
        val themes = fetchSetThemes(context)
        return themes.find { it.id == themeId }?.name ?: "Unknown Theme"
    }

    fun fetchSetsPage(themeId: Int?, page: Int, pageSize: Int, context: Context) {
        val offset = (page - 1) * pageSize
        viewModelScope.launch(Dispatchers.IO) {
            val (setsList, total) = DatabaseHelper.getSetsPage(context, themeId, offset, pageSize)
            withContext(Dispatchers.Main) {
                _sets.value = setsList
                _totalSets.value = total
                _currentPage.value = page
            }
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

    fun loadSetInfo(context: Context, setNum: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val info = DatabaseHelper.getSetInfo(context, setNum)
            withContext(Dispatchers.Main) {
                _setName.value = info.name
                _setYear.value = info.year
                _setNumParts.value = info.totalParts
                _setInventories.value = info.inventories
                _selectedInventory.value = info.inventories.firstOrNull()
                _setImageUrl.value = info.imgUrl
            }
        }
    }

    fun selectInventory(index: Int) {
        _selectedInventory.value = _setInventories.value.getOrNull(index)
    }
}