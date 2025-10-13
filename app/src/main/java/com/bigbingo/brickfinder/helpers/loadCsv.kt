package com.bigbingo.brickfinder.helpers

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

fun <T> loadCsv(
    context: Context,
    fileName: String,
    parseLine: (List<String>) -> T?
): List<T> {
    val list = mutableListOf<T>()
    val inputStream = context.assets.open(fileName)
    val reader = BufferedReader(InputStreamReader(inputStream))

    reader.useLines { lines ->
        lines.drop(1).forEach { line ->
            val tokens = line.split(",")
            val item = parseLine(tokens)
            if (item != null) list.add(item)
        }
    }

    return list
}
