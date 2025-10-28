package com.bigbingo.brickfinder.data

data class Part(
    val part_num: String,
    val name: String,
    val part_cat_id: Int,
    val part_url: String? = null,
    val part_img_url: String? = null,
    val external_ids: Map<String, List<String>>? = null
)