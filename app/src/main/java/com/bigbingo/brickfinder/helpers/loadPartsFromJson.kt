package com.bigbingo.brickfinder.helpers

import android.content.Context
import com.bigbingo.brickfinder.data.Part
import org.json.JSONObject

fun loadPartsFromJson(context: Context, filename: String): List<Part> {
    val parts = mutableListOf<Part>()
    try {
        val jsonString = context.assets.open(filename)
            .bufferedReader()
            .use { it.readText() }

        val jsonObject = JSONObject(jsonString)
        val results = jsonObject.getJSONArray("results")

        for (i in 0 until results.length()) {
            val item = results.getJSONObject(i)
            val partNum = item.getString("part_num")
            val name = item.getString("name")
            val partCatId = item.getInt("part_cat_id")
            val partUrl = item.getString("part_url")
            val partImgUrl = item.getString("part_img_url")

            parts.add(Part(partNum, name, partCatId, partUrl, partImgUrl))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return parts
}