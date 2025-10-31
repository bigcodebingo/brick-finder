package com.bigbingo.brickfinder.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.bigbingo.brickfinder.data.PartColor
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartAppearance
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
        val cursor = db.rawQuery("SELECT id, name, parent_id FROM sets_themes ORDER BY name", null)
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
        return themes
    }

    fun getSetsPage(context: Context, themeId: Int?, offset: Int, limit: Int): Pair<List<Set>, Int> {
        val db = getDatabase(context)

        val countQuery = if (themeId != null) {
            """
        SELECT COUNT(*)
        FROM sets
        WHERE theme_id = ?
           OR theme_id IN (
               SELECT id
               FROM sets_themes
               WHERE parent_id = ?
           )
        """.trimIndent()
        } else {
            "SELECT COUNT(*) FROM sets"
        }

        val countCursor = if (themeId != null) {
            db.rawQuery(countQuery, arrayOf(themeId.toString(), themeId.toString()))
        } else {
            db.rawQuery(countQuery, null)
        }
        countCursor.moveToFirst()
        val total = countCursor.getInt(0)
        countCursor.close()

        val selectQuery = if (themeId != null) {
            """
        SELECT set_num, name, year, theme_id, num_parts, set_img_url, set_url
        FROM sets
        WHERE theme_id = ?
           OR theme_id IN (
               SELECT id
               FROM sets_themes
               WHERE parent_id = ?
           )
           ORDER BY year ASC, name ASC
        LIMIT ? OFFSET ?
        """.trimIndent()
        } else {
            "SELECT set_num, name, year, theme_id, num_parts, set_img_url, set_url FROM sets ORDER BY year ASC, " +
                    "name ASC LIMIT ? OFFSET ?"
        }

        val cursor = if (themeId != null) {
            db.rawQuery(selectQuery, arrayOf(themeId.toString(), themeId.toString(), limit.toString(), offset.toString()))
        } else {
            db.rawQuery(selectQuery, arrayOf(limit.toString(), offset.toString()))
        }

        val setsList = mutableListOf<Set>()
        while (cursor.moveToNext()) {
            val set_img_url = cursor.getString(5)
                ?: "https://cdn.rebrickable.com/media/thumbs/nil.png/85x85p.png?1662040927.7130826"
            setsList.add(
                Set(
                    set_num = cursor.getString(0),
                    name = cursor.getString(1),
                    year = cursor.getInt(2),
                    theme_id = cursor.getString(3),
                    num_parts = cursor.getString(4),
                    set_img_url = set_img_url,
                    set_url = cursor.getString(6)
                )
            )
        }

        cursor.close()
        db.close()
        return Pair(setsList, total)
    }

    fun getPartByNum(context: Context, partNum: String): Part? {
        val db = getDatabase(context)
        val cursor = db.rawQuery(
            "SELECT part_num, name, part_cat_id, part_url, part_img_url FROM parts WHERE part_num = ?",
            arrayOf(partNum)
        )

        var part: Part? = null
        if (cursor.moveToFirst()) {
            part = Part(
                part_num = cursor.getString(0),
                name = cursor.getString(1),
                part_cat_id = cursor.getInt(2),
                part_url = cursor.getString(3),
                part_img_url = cursor.getString(4),
            )
        }
        cursor.close()
        db.close()
        return part
    }

    data class ColorCount(
        val colorId: Int,
        val count: Int
    )

    fun getPartInfo(
        context: Context,
        partNum: String
    ): Triple<List<String>, Pair<Int?, Int?>, List<ColorCount>> {
        val db = getDatabase(context)

        val setNums = mutableListOf<String>()
        val setsQuery = """
        SELECT DISTINCT inv.set_num
        FROM inventory_parts AS ip
        INNER JOIN inventories AS inv
            ON ip.inventory_id = inv.id
        WHERE ip.part_num = ?
    """.trimIndent()

        val cursorSets = db.rawQuery(setsQuery, arrayOf(partNum))
        while (cursorSets.moveToNext()) {
            setNums.add(cursorSets.getString(0))
        }
        cursorSets.close()

        var minYear: Int? = null
        var maxYear: Int? = null
        if (setNums.isNotEmpty()) {
            val yearQuery = """
            SELECT MIN(s.year), MAX(s.year)
            FROM sets AS s
            WHERE s.set_num IN (${setNums.joinToString(",") { "'$it'" }})
        """.trimIndent()

            val cursorYear = db.rawQuery(yearQuery, null)
            if (cursorYear.moveToFirst()) {
                if (!cursorYear.isNull(0)) minYear = cursorYear.getInt(0)
                if (!cursorYear.isNull(1)) maxYear = cursorYear.getInt(1)
            }
            cursorYear.close()
        }

        val colorCounts = mutableListOf<ColorCount>()
        val colorQuery = """
        SELECT ip.color_id, COUNT(DISTINCT inv.set_num) AS set_count
        FROM inventory_parts AS ip
        INNER JOIN inventories AS inv
            ON ip.inventory_id = inv.id
        WHERE ip.part_num = ?
        GROUP BY ip.color_id
    """.trimIndent()

        val cursorColors = db.rawQuery(colorQuery, arrayOf(partNum))
        while (cursorColors.moveToNext()) {
            val colorId = cursorColors.getInt(0)
            val count = cursorColors.getInt(1)
            colorCounts.add(ColorCount(colorId, count))
        }
        cursorColors.close()

        db.close()
        return Triple(setNums, Pair(minYear, maxYear), colorCounts)
    }

    fun getColorsByIds(context: Context, colorIds: List<Int>): List<PartColor> {
        if (colorIds.isEmpty()) return emptyList()
        val db = getDatabase(context)
        val idsString = colorIds.joinToString(",")
        val query = "SELECT id, name, rgb, is_trans FROM colors WHERE id IN ($idsString)"
        val cursor = db.rawQuery(query, null)
        val colors = mutableListOf<PartColor>()
        while (cursor.moveToNext()) {
            colors.add(
                PartColor(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    rgb = cursor.getString(2),
                    isTrans = cursor.getInt(3) != 0
                )
            )
        }
        cursor.close()
        db.close()
        return colors
    }

    fun getPartAppearancesInSets(context: Context, partNum: String, setNums: List<String>): List<PartAppearance> {
        if (setNums.isEmpty()) return emptyList()
        val db = getDatabase(context)
        val placeholders = setNums.joinToString(",") { "?" }

        val query = """
        SELECT s.set_num, s.name, s.year, s.theme_id, s.num_parts, s.set_img_url,
               ip.quantity, ip.color_id
        FROM inventory_parts AS ip
        JOIN inventories AS inv ON ip.inventory_id = inv.id
        LEFT JOIN inventory_minifigs AS imf ON inv.set_num = imf.fig_num
        JOIN inventories AS inv_real 
            ON inv_real.id = COALESCE(imf.inventory_id, inv.id)
        JOIN sets AS s ON inv_real.set_num = s.set_num
        WHERE ip.part_num = ?
          AND inv_real.set_num IN ($placeholders)
    """.trimIndent()

        val args = mutableListOf<String>()
        args.add(partNum)
        args.addAll(setNums)

        val cursor = db.rawQuery(query, args.toTypedArray())
        val tempData = mutableListOf<Triple<Set, Int, Int>>() // Set, quantity, colorId
        val colorIds = mutableSetOf<Int>()

        while (cursor.moveToNext()) {
            val set = Set(
                set_num = cursor.getString(0),
                name = cursor.getString(1),
                year = cursor.getInt(2),
                theme_id = cursor.getString(3),
                num_parts = cursor.getString(4),
                set_img_url = cursor.getString(5)
            )

            val quantity = cursor.getInt(6)
            val colorId = cursor.getInt(7)
            colorIds.add(colorId)

            tempData.add(Triple(set, quantity, colorId))
        }

        cursor.close()
        db.close()

        val colors = getColorsByIds(context, colorIds.toList())
        val colorMap = colors.associateBy { it.id }

        val appearances = tempData.mapNotNull { (set, quantity, colorId) ->
            val color = colorMap[colorId] ?: return@mapNotNull null
            PartAppearance(
                set = set,
                quantity = quantity,
                color = color
            )
        }

        return appearances.distinctBy { it.set.set_num to it.color.id }
    }
}