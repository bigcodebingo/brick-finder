package com.bigbingo.brickfinder.helpers

import android.content.Context
import com.opencsv.CSVReader
import java.io.InputStreamReader

fun <T> loadCsv(
    context: Context,
    fileName: String,
    parseLine: (List<String>) -> T?
): List<T> {
    val list = mutableListOf<T>()
    context.assets.open(fileName).use { inputStream ->
        CSVReader(InputStreamReader(inputStream)).use { reader ->
            val allLines = reader.readAll()
            allLines.drop(1).forEach { tokens ->
                parseLine(tokens.toList())?.let { list.add(it) }
            }
        }
    }
    return list
}

