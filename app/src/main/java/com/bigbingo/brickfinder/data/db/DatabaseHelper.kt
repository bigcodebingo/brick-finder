package com.bigbingo.brickfinder.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartCategory

object DatabaseHelper {

    fun getDatabase(context: Context, dbName: String = "brickfinder.db"): SQLiteDatabase {
        val dbFile = context.getDatabasePath(dbName)
        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            context.assets.open(dbName).use { input ->
                dbFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    fun getCategories(context: Context): List<PartCategory> {
        val categories = mutableListOf<PartCategory>()
        val db = getDatabase(context)
        val cursor = db.rawQuery("SELECT id, name, imageUrl FROM categories", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"))
                categories.add(PartCategory(id, name, imageUrl))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return categories.sortedBy { it.name }
    }

    fun getPartsPage(context: Context, categoryId: Int, offset: Int, limit: Int): Pair<List<Part>, Int> {
        val db = context.openOrCreateDatabase("brickfinder.db", Context.MODE_PRIVATE, null)

        val cursorTotal = db.rawQuery(
            "SELECT COUNT(*) FROM parts WHERE part_cat_id = ?",
            arrayOf(categoryId.toString())
        )
        cursorTotal.moveToFirst()
        val total = cursorTotal.getInt(0)
        cursorTotal.close()

        val cursor = db.rawQuery(
            "SELECT part_num, name, part_cat_id, part_url, part_img_url FROM parts WHERE part_cat_id = ? LIMIT ? OFFSET ?",
            arrayOf(categoryId.toString(), limit.toString(), offset.toString())
        )

        val partsList = mutableListOf<Part>()
        while (cursor.moveToNext()) {
            val imageUrl = cursor.getString(4)
                ?: "https://cdn.rebrickable.com/media/thumbs/nil.png/85x85p.png?1662040927.7130826"
            partsList.add(
                Part(
                    part_num = cursor.getString(0),
                    name = cursor.getString(1),
                    part_cat_id = cursor.getInt(2),
                    part_url = cursor.getString(3),
                    part_img_url = imageUrl
                )
            )
        }

        cursor.close()
        db.close()
        return Pair(partsList, total)
    }
}