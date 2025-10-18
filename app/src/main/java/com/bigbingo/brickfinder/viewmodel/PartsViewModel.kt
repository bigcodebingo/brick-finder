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

    private val _categories = MutableStateFlow<List<PartCategory>>(emptyList())
    val categories: StateFlow<List<PartCategory>> = _categories
    private val _parts = MutableStateFlow<List<Part>>(emptyList())
    val parts: StateFlow<List<Part>> = _parts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun clearParts() {
        _parts.value = emptyList()
    }

    fun fetchCategoriesFromDb(context: Context) {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) {
                DatabaseHelper.getCategories(context)
            }
            _categories.value = list
        }
    }

    fun fetchPartsFromDb(context: Context, categoryId: Int? = null) {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) {
                DatabaseHelper.getParts(context, categoryId)
            }
            _parts.value = list
        }
    }
}
