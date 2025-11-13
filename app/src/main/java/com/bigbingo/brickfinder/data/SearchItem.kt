package com.bigbingo.brickfinder.data

enum class ItemType {
    SET, PART
}

data class SearchItem(
    val itemNum: String,
    val name: String?,
    val type: ItemType,
    val year: Int? = null,
    val numParts: Int? = null,
    val imageUrl: String? = null
)