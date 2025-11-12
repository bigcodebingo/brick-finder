package com.bigbingo.brickfinder.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.bigbingo.brickfinder.data.PartColorCount
import com.bigbingo.brickfinder.data.PartColor
import com.bigbingo.brickfinder.data.Part
import com.bigbingo.brickfinder.data.PartAppearance
import com.bigbingo.brickfinder.data.PartCategory
import com.bigbingo.brickfinder.data.SetTheme
import com.bigbingo.brickfinder.data.Set
import com.bigbingo.brickfinder.data.SetInfo
import com.bigbingo.brickfinder.data.SetInventory

object DatabaseHelper {

    private const val DEFAULT_IMG_URL =
        "https://cdn.rebrickable.com/media/thumbs/nil.png/85x85p.png?1662040927.7130826"

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
                val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")) ?: DEFAULT_IMG_URL
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
            val rawImg = cursor.getString(4)
            val imageUrl = if (rawImg.isNullOrBlank()) DEFAULT_IMG_URL else rawImg

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

    fun getThemeIdBySetNum(context: Context, setNum: String): Int? {
        val db = getDatabase(context)
        val cursor = db.rawQuery(
            "SELECT theme_id FROM sets WHERE set_num = ?",
            arrayOf(setNum)
        )
        val themeId = if (cursor.moveToFirst()) {
            if (cursor.isNull(0)) null else cursor.getInt(0)
        } else {
            null
        }
        cursor.close()
        db.close()
        return themeId
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
            val rawImg = cursor.getString(5)
            val set_img_url = if (rawImg.isNullOrBlank()) DEFAULT_IMG_URL else rawImg

            setsList.add(
                Set(
                    set_num = cursor.getString(0),
                    name = cursor.getString(1),
                    year = cursor.getInt(2),
                    theme_id = cursor.getString(3),
                    num_parts = cursor.getInt(4),
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
                part_img_url = cursor.getString(4) ?: DEFAULT_IMG_URL
            )
        }
        cursor.close()
        db.close()
        return part
    }

    fun getPartInfo(
        context: Context,
        partNum: String
    ): Triple<List<String>, Pair<Int?, Int?>, List<PartColorCount>> {
        val db = getDatabase(context)

        val setNums = mutableListOf<String>()
        val setsQuery = """
        SELECT DISTINCT COALESCE(inv_parent.set_num, inv.set_num) AS real_set_num
        FROM inventory_parts AS ip
        JOIN inventories AS inv ON ip.inventory_id = inv.id
        LEFT JOIN inventory_minifigs AS imf ON imf.fig_num = inv.set_num
        LEFT JOIN inventories AS inv_parent ON inv_parent.id = imf.inventory_id
        WHERE ip.part_num = ?
          AND (inv_parent.set_num IS NOT NULL OR inv.set_num NOT LIKE 'fig-%')
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

        val colorCounts = mutableListOf<PartColorCount>()
        val colorQuery = """
        SELECT 
            ip.color_id, 
            COUNT(DISTINCT COALESCE(inv_parent.set_num, inv.set_num)) AS set_count
        FROM inventory_parts AS ip
        JOIN inventories AS inv 
            ON ip.inventory_id = inv.id
        LEFT JOIN inventory_minifigs AS imf 
            ON imf.fig_num = inv.set_num
        LEFT JOIN inventories AS inv_parent 
            ON inv_parent.id = imf.inventory_id
        WHERE ip.part_num = ?
          AND (inv_parent.set_num IS NOT NULL OR inv.set_num NOT LIKE 'fig-%')
        GROUP BY ip.color_id
        """.trimIndent()

        val cursorColors = db.rawQuery(colorQuery, arrayOf(partNum))
        while (cursorColors.moveToNext()) {
            val colorId = cursorColors.getInt(0)
            val count = cursorColors.getInt(1)

            colorCounts.add(PartColorCount(colorId, count))
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
        SELECT 
            s.set_num, s.name, s.year, s.theme_id, s.num_parts, s.set_img_url,
            ip.color_id
        FROM inventory_parts AS ip
        JOIN inventories AS inv ON ip.inventory_id = inv.id
        LEFT JOIN inventory_minifigs AS imf ON imf.fig_num = inv.set_num
        LEFT JOIN inventories AS inv_parent ON inv_parent.id = imf.inventory_id
        JOIN sets AS s ON s.set_num = COALESCE(inv_parent.set_num, inv.set_num)
        WHERE ip.part_num = ?
          AND COALESCE(inv_parent.set_num, inv.set_num) IN ($placeholders)
    """.trimIndent()

        val args = mutableListOf<String>().apply {
            add(partNum)
            addAll(setNums)
        }

        val cursor = db.rawQuery(query, args.toTypedArray())
        val tempData = mutableListOf<Pair<Set, Int>>()
        val colorIds = mutableSetOf<Int>()

        while (cursor.moveToNext()) {
            val rawImg = cursor.getString(5)
            val img = if (rawImg.isNullOrBlank()) DEFAULT_IMG_URL else rawImg

            val set = Set(
                set_num = cursor.getString(0),
                name = cursor.getString(1),
                year = cursor.getInt(2),
                theme_id = cursor.getString(3),
                num_parts = cursor.getInt(4),
                set_img_url = img
            )
            val colorId = cursor.getInt(6)
            colorIds.add(colorId)
            tempData.add(set to colorId)
        }
        cursor.close()

        val colors = getColorsByIds(context, colorIds.toList())
        val colorMap = colors.associateBy { it.id }

        val qtyMap = mutableMapOf<Pair<String, Int>, Int>()

        val queryParts = """
        SELECT inv.set_num, ip.color_id, ip.quantity
        FROM inventory_parts AS ip
        JOIN inventories AS inv ON ip.inventory_id = inv.id
        WHERE inv.set_num IN ($placeholders)
          AND ip.part_num = ?
          AND ip.is_spare = 0
        GROUP BY inv.set_num, ip.color_id
        """.trimIndent()

        val argsParts = mutableListOf<String>().apply {
            addAll(setNums)
            add(partNum)
        }

        val cursorParts = db.rawQuery(queryParts, argsParts.toTypedArray())
        while (cursorParts.moveToNext()) {
            val setNum = cursorParts.getString(0)
            val colorId = cursorParts.getInt(1)
            val qty = cursorParts.getInt(2)
            qtyMap[setNum to colorId] = qty
        }
        cursorParts.close()

        val queryMinifigs = """
        SELECT inv.set_num, ip.color_id, SUM(ip.quantity * imf.quantity) AS total_qty
        FROM inventories AS inv
        JOIN inventory_minifigs AS imf ON imf.inventory_id = inv.id
        JOIN inventories AS figInv ON figInv.set_num = imf.fig_num
        JOIN inventory_parts AS ip ON ip.inventory_id = figInv.id
        WHERE inv.set_num IN ($placeholders)
          AND ip.part_num = ?
          AND (ip.is_spare IS NULL OR ip.is_spare = 0)
          AND inv.id = (
              SELECT MIN(inv2.id)
              FROM inventories AS inv2
              WHERE inv2.set_num = inv.set_num
          )
        GROUP BY inv.set_num, ip.color_id
    """.trimIndent()

        val argsMinifigs = mutableListOf<String>().apply {
            addAll(setNums)
            add(partNum)
        }

        val cursorMinifigs = db.rawQuery(queryMinifigs, argsMinifigs.toTypedArray())
        while (cursorMinifigs.moveToNext()) {
            val setNum = cursorMinifigs.getString(0)
            val colorId = cursorMinifigs.getInt(1)
            val qty = cursorMinifigs.getInt(2)
            qtyMap[setNum to colorId] = (qtyMap[setNum to colorId] ?: 0) + qty
        }
        cursorMinifigs.close()
        db.close()

        return tempData.mapNotNull { (set, colorId) ->
            val color = colorMap[colorId] ?: return@mapNotNull null
            val totalQty = qtyMap[set.set_num to colorId] ?: 0
            PartAppearance(set = set, quantity = totalQty, color = color)
        }
            .distinctBy { it.set.set_num to it.color.id }
            .sortedBy { it.set.year }
    }

    fun getSetInfo(context: Context, setNum: String): SetInfo {
        val db = getDatabase(context)

        val setCursor = db.rawQuery(
            "SELECT year, num_parts, set_img_url FROM sets WHERE set_num = ?",
            arrayOf(setNum)
        )
        if (!setCursor.moveToFirst()) throw Exception("Set not found: $setNum")
        val year = setCursor.getInt(0)
        val numParts = setCursor.getInt(1)
        val rawImg = setCursor.getString(2)
        val imgUrl = if (rawImg.isNullOrBlank()) DEFAULT_IMG_URL else rawImg
        setCursor.close()

        val inventories = mutableListOf<SetInventory>()
        val invCursor = db.rawQuery(
            "SELECT id, version FROM inventories WHERE set_num = ?",
            arrayOf(setNum)
        )

        while (invCursor.moveToNext()) {
            val invId = invCursor.getInt(0)
            val version = invCursor.getInt(1)

            val parts = mutableListOf<Triple<String, String?, Int>>()
            val partCursor = db.rawQuery(
                """
            SELECT part_num, img_url, quantity
            FROM inventory_parts
            WHERE inventory_id = ? AND (is_spare IS NULL OR is_spare = 0)
            """,
                arrayOf(invId.toString())
            )
            while (partCursor.moveToNext()) {
                val rawImg = partCursor.getString(1)
                val img = if (rawImg.isNullOrBlank()) DEFAULT_IMG_URL else rawImg

                parts.add(Triple(partCursor.getString(0), img, partCursor.getInt(2)))

            }
            partCursor.close()

            val minifigs = mutableListOf<Triple<String, String?, Int>>()
            val minifigPartsTemp = mutableListOf<Triple<String, String?, Int>>()

            val figCursor = db.rawQuery(
                """
            SELECT inventory_minifigs.fig_num, minifigs.img_url, inventory_minifigs.quantity
            FROM inventory_minifigs
            JOIN minifigs ON inventory_minifigs.fig_num = minifigs.fig_num
            WHERE inventory_minifigs.inventory_id = ?
            """,
                arrayOf(invId.toString())
            )

            while (figCursor.moveToNext()) {
                val figNum = figCursor.getString(0)
                val rawFigImg = figCursor.getString(1)
                val figImg = if (rawFigImg.isNullOrBlank()) DEFAULT_IMG_URL else rawFigImg
                val figQty = figCursor.getInt(2)
                minifigs.add(Triple(figNum, figImg, figQty))

                val figPartsCursor = db.rawQuery(
                    """
                SELECT inventory_parts.part_num, inventory_parts.img_url, inventory_parts.quantity
                FROM inventory_parts
                JOIN inventories ON inventory_parts.inventory_id = inventories.id
                WHERE inventories.set_num = ?
                  AND (inventory_parts.is_spare IS NULL OR inventory_parts.is_spare = 0)
                """,
                    arrayOf(figNum)
                )

                while (figPartsCursor.moveToNext()) {
                    minifigPartsTemp.add(
                        Triple(
                            figPartsCursor.getString(0),
                            figPartsCursor.getString(1),
                            figPartsCursor.getInt(2) * figQty
                        )
                    )
                }
                figPartsCursor.close()
            }
            figCursor.close()

            val minifigPartsMap = mutableMapOf<Pair<String, String?>, Int>()
            for ((partNum, imgUrl, qty) in minifigPartsTemp) {
                val key = partNum to imgUrl
                minifigPartsMap[key] = (minifigPartsMap[key] ?: 0) + qty
            }

            val minifigParts = minifigPartsMap.map { (key, qty) ->
                Triple(key.first, key.second, qty)
            }

            inventories.add(SetInventory(invId, version, parts, minifigs, minifigParts))
        }

        invCursor.close()
        db.close()

        return SetInfo(setNum, year, numParts, inventories, imgUrl)
    }
}