package com.bigbingo.brickfinder.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbingo.brickfinder.data.ItemType
import com.bigbingo.brickfinder.data.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.bigbingo.brickfinder.helpers.WantedListRepository

class WantedListViewModel : ViewModel() {
    private val _wantedItems = MutableStateFlow<List<Item>>(emptyList())
    val wantedItems: StateFlow<List<Item>> = _wantedItems

    fun loadWantedItems(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val items = WantedListRepository.getAllWantedItems(context)
                _wantedItems.value = items
            }
        }
    }

    fun addToWantedList(context: Context, item: Item) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                WantedListRepository.addToWantedList(context, item)
                loadWantedItems(context)
            }
        }
    }

    fun removeFromWantedList(context: Context, itemNum: String, type: ItemType) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                WantedListRepository.removeFromWantedList(context, itemNum, type)
                loadWantedItems(context)
            }
        }
    }

    suspend fun isInWantedList(context: Context, itemNum: String, type: ItemType): Boolean {
        return withContext(Dispatchers.IO) {
            WantedListRepository.isInWantedList(context, itemNum, type)
        }
    }
}

