package com.bigbingo.brickfinder.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartCategory
import com.bigbingo.brickfinder.data.SetTheme
import com.bigbingo.brickfinder.data.Set


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

    fun getPartCategories(context: Context): List<PartCategory> {
        val categories = mutableListOf<PartCategory>()
        val db = getDatabase(context)
        val cursor = db.rawQuery("SELECT id, name, imageUrl FROM part_categories", null)

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

    fun getAllSetThemes(context: Context): List<SetTheme> {
        val db = getDatabase(context)
        val cursor = db.rawQuery("SELECT id, name, parent_id FROM sets_themes", null)
        val themes = mutableListOf<SetTheme>()
        if (cursor.moveToFirst()) {
            do {
                themes.add(
                    SetTheme(
                        id = cursor.getInt(0),
                        name = cursor.getString(1),
                        parent_id = if (cursor.isNull(2)) null else cursor.getInt(2)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return themes.sortedBy { setTheme -> setTheme.name }
    }

    fun getSetsPage(context: Context, themeId: Int?, offset: Int, limit: Int): Pair<List<Set>, Int> {
        val db = getDatabase(context)

        val countQuery = if (themeId != null) {
            "SELECT COUNT(*) FROM sets WHERE theme_id = ?"
        } else {
            "SELECT COUNT(*) FROM sets"
        }
        val countCursor = if (themeId != null) {
            db.rawQuery(countQuery, arrayOf(themeId.toString()))
        } else {
            db.rawQuery(countQuery, null)
        }
        countCursor.moveToFirst()
        val total = countCursor.getInt(0)
        countCursor.close()

        val selectQuery = if (themeId != null) {
            "SELECT set_num, name, year, theme_id, num_parts, set_img_url, set_url " +
                    "FROM sets WHERE theme_id = ? LIMIT ? OFFSET ?"
        } else {
            "SELECT set_num, name, year, theme_id, num_parts, set_img_url, set_url " +
                    "FROM sets LIMIT ? OFFSET ?"
        }

        val cursor = if (themeId != null) {
            db.rawQuery(selectQuery, arrayOf(themeId.toString(), limit.toString(), offset.toString()))
        } else {
            db.rawQuery(selectQuery, arrayOf(limit.toString(), offset.toString()))
        }

        val setsList = mutableListOf<Set>()
        while (cursor.moveToNext()) {
            setsList.add(
                Set(
                    set_num = cursor.getString(0),
                    name = cursor.getString(1),
                    year = cursor.getInt(2),
                    theme_id = cursor.getString(3),
                    num_parts = cursor.getString(4),
                    set_img_url = cursor.getString(5),
                    set_url = cursor.getString(6)
                )
            )
        }

        cursor.close()
        db.close()
        return Pair(setsList, total)
    }
}