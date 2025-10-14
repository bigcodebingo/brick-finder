package com.bigbingo.brickfinder.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbingo.brickfinder.data.PartCategory
import com.bigbingo.brickfinder.helpers.loadCsv
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PartsViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<PartCategory>>(emptyList())
    val categories: StateFlow<List<PartCategory>> = _categories

    fun fetchCategoriesFromCsv(context: Context) {
        viewModelScope.launch {
            val categories = loadCsv(context, "part_categories.csv") { tokens ->
                if (tokens.size >= 4) {
                    val id = tokens[0].toIntOrNull() ?: 0
                    val name = tokens[1]
                    val partCount = tokens[2].toIntOrNull() ?: 0
                    val imageUrl = tokens[3]
                    PartCategory(id, name, partCount, imageUrl)
                } else null
            }

            _categories.value = categories
        }
    }
}
