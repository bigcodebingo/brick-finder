package com.bigbingo.brickfinder.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.bigbingo.brickfinder.data.Item
import com.bigbingo.brickfinder.helpers.DatabaseHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class HomeViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")

    var searchResults = mutableStateListOf< Item>()
        private set

    fun onSearchQueryChange(query: String, context: Context) {
        searchQuery = query
        if (query.isBlank()) {
            searchResults.clear()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val results = DatabaseHelper.searchLegoItems(context, query)
            withContext(Dispatchers.Main) {
                searchResults.clear()
                searchResults.addAll(results)
            }
        }
    }
}
