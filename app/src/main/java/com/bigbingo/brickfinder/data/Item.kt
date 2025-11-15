package com.bigbingo.brickfinder.data

enum class ItemType {
    SET, PART
}

data class Item(
    val id: Long = 0,
    val itemNum: String,
    val name: String?,
    val type: ItemType,
    val imageUrl: String? = null,
    val year: Int? = null,
    val numParts: Int? = null
)