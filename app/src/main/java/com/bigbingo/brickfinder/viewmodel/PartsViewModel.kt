package com.bigbingo.brickfinder.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.bigbingo.brickfinder.data.db.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PartsViewModel : ViewModel() {

    private val _allCategories = MutableStateFlow<List<PartCategory>>(emptyList())
    private val _categories = MutableStateFlow<List<PartCategory>>(emptyList())
    val categories: StateFlow<List<PartCategory>> = _categories
    private val _parts = MutableStateFlow<List<Part>>(emptyList())
    val parts: StateFlow<List<Part>> = _parts
    private val _totalParts = MutableStateFlow(0)
    val totalParts: StateFlow<Int> = _totalParts
    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    fun clearParts() {
        _parts.value = emptyList()
        _totalParts.value = 0
    }

    fun resetCurrentPage() {
        _currentPage.value = 1
    }

    fun filterCategories(filter: String) {
        _selectedFilter.value = filter
        val all = _allCategories.value
        _categories.value = when (filter) {
            "Popular" -> all.filter { it.id in listOf(1,11,37,20,3,5,6,62,64,63,59,61,60,23,14,49,
                9,51,8,12,52,40,26,25,19,67,15,36,35,29,16) }
            "Minifigs" -> all.filter { it.name.contains("Minifig", ignoreCase = true) }
            "Technic" -> all.filter { it.name.contains("Technic", ignoreCase = true) }
            "Old" -> all.filter { it.id in listOf(42,48,50,66,43,78) }
            else -> all
        }
    }

    fun fetchCategoriesFromDb(context: Context) {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) {
                DatabaseHelper.getPartCategories(context)
            }
            _allCategories.value = list
            filterCategories(_selectedFilter.value)
        }
    }

    fun fetchPartsPage(categoryId: Int, page: Int, pageSize: Int, context: Context) {
        val offset = (page - 1) * pageSize
        viewModelScope.launch(Dispatchers.IO) {
            val (partsList, total) = DatabaseHelper.getPartsPage(context, categoryId, offset, pageSize)
            withContext(Dispatchers.Main) {
                _parts.value = partsList
                _totalParts.value = total
                _currentPage.value = page
            }
        }
    }
}
