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

    fun getParts(context: Context, categoryId: Int? = null): List<Part> {
        val parts = mutableListOf<Part>()
        val db = getDatabase(context)

        val query = if (categoryId != null) {
            "SELECT part_num, name, part_cat_id, part_url, part_img_url FROM parts WHERE part_cat_id = ?"
        } else {
            "SELECT part_num, name, part_cat_id, part_url, part_img_url FROM parts"
        }

        val cursor = if (categoryId != null) {
            db.rawQuery(query, arrayOf(categoryId.toString()))
        } else {
            db.rawQuery(query, null)
        }

        if (cursor.moveToFirst()) {
            do {
                val partNum = cursor.getString(cursor.getColumnIndexOrThrow("part_num"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val catId = cursor.getInt(cursor.getColumnIndexOrThrow("part_cat_id"))
                val partUrl = cursor.getString(cursor.getColumnIndexOrThrow("part_url"))
                val partImgUrl = cursor.getString(cursor.getColumnIndexOrThrow("part_img_url"))

                parts.add(Part(partNum, name, catId, partUrl, partImgUrl))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return parts
    }
}