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

    fun fetchPartsPage(categoryId: Int, offset: Int, limit: Int, context: Context): Pair<List<Part>, Int> {
        val db = context.openOrCreateDatabase("brickfinder.db", Context.MODE_PRIVATE, null)
        val cursorTotal = db.rawQuery(
            "SELECT COUNT(*) FROM parts WHERE part_cat_id = ?",
            arrayOf(categoryId.toString())
        )
        cursorTotal.moveToFirst()
        val total = cursorTotal.getInt(0)
        cursorTotal.close()

        val cursor = db.rawQuery(
            "SELECT part_num, name, part_cat_id, part_url, part_img_url FROM parts WHERE part_cat_id = ? LIMIT ? OFFSET ?",
            arrayOf(categoryId.toString(), limit.toString(), offset.toString())
        )

        val partsList = mutableListOf<Part>()
        while (cursor.moveToNext()) {
            val imageUrl = cursor.getString(4) ?: "https://cdn.rebrickable.com/media/thumbs/nil.png/85x85p.png?1662040927.7130826"
            partsList.add(
                Part(
                    part_num = cursor.getString(0),
                    name = cursor.getString(1),
                    part_cat_id = cursor.getInt(2),
                    part_url = cursor.getString(3),
                    part_img_url = imageUrl
                )
            )
        }
        cursor.close()
        db.close()
        return Pair(partsList, total)
    }
}
