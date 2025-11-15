package com.bigbingo.brickfinder.ui.screens.home

import android.content.Context
import com.bigbingo.brickfinder.data.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

object SearchHistoryManager {
    private const val PREF = "search_history"
    private const val KEY = "items"
    private const val MAX_ITEMS = 12

    private val gson = Gson()
    private val typeToken = object : TypeToken<MutableList<Item>>() {}.type

    fun add(context: Context, item: Item) {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY, null)

        val list: MutableList<Item> =
            if (json != null) gson.fromJson(json, typeToken) else mutableListOf()

        list.removeAll { it.itemNum == item.itemNum }

        list.add(0, item)

        if (list.size > MAX_ITEMS) list.removeAt(list.size - 1)

        prefs.edit { putString(KEY, gson.toJson(list))}
    }

    fun get(context: Context): List<Item> {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY, null) ?: return emptyList()
        return gson.fromJson(json, typeToken)
    }
}