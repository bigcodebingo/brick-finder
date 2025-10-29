package com.bigbingo.brickfinder.data

data class PartColor(
    val id: Int,
    val name: String,
    val rgb: String,
    val isTrans: Boolean,
    val count: Int = 0
)