package com.bigbingo.brickfinder.data

data class Set (
    val set_num: String,
    val name: String,
    val year: Int,
    val theme_id: String? = null,
    val num_parts: Int? = null,
    val set_img_url: String? = null,
    val set_url: String? = null
)