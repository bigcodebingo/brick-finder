package com.bigbingo.brickfinder.data

data class SetInventory(
    val id: Int,
    val version: Int,
    val parts: List<Triple<String, String?, Int>>,
    val minifigs: List<Triple<String, String?, Int>>
)
data class SetInfo(
    val setNum: String,
    val year: Int,
    val totalParts: Int,
    val inventories: List<SetInventory>
)