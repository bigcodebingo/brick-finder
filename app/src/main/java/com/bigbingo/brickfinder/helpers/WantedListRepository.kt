package com.bigbingo.brickfinder.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.bigbingo.brickfinder.data.ItemType
import com.bigbingo.brickfinder.data.Item

object WantedListRepository {

    fun addToWantedList(context: Context, item: Item): Long {
        val db = DatabaseHelper.getDatabase(context)
        val values = ContentValues().apply {
            put("item_num", item.itemNum)
            put("name", item.name)
            put("type", item.type.name)
            put("image_url", item.imageUrl)
            put("year", item.year)
            put("num_parts", item.numParts)
        }
        val result = db.insertWithOnConflict(
            "wanted_list",
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE
        )
        db.close()
        return result
    }

    fun removeFromWantedList(context: Context, itemNum: String, type: ItemType): Boolean {
        val db = DatabaseHelper.getDatabase(context)
        val deleted = db.delete(
            "wanted_list",
            "item_num = ? AND type = ?",
            arrayOf(itemNum, type.name)
        ) > 0
        db.close()
        return deleted
    }

    fun isInWantedList(context: Context, itemNum: String, type: ItemType): Boolean {
        val db = DatabaseHelper.getDatabase(context)
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM wanted_list WHERE item_num = ? AND type = ?",
            arrayOf(itemNum, type.name)
        )
        val count = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        db.close()
        return count > 0
    }

    fun getAllWantedItems(context: Context): List<Item> {
        val db = DatabaseHelper.getDatabase(context)
        val cursor = db.rawQuery(
            "SELECT id, item_num, name, type, image_url, year, num_parts FROM wanted_list ORDER BY id DESC",
            null
        )
        val items = mutableListOf<Item>()
        while (cursor.moveToNext()) {
            items.add(
                Item(
                    id = cursor.getLong(0),
                    itemNum = cursor.getString(1),
                    name = cursor.getString(2),
                    type = ItemType.valueOf(cursor.getString(3)),
                    imageUrl = cursor.getStringOrNull(4),
                    year = cursor.getIntOrNull(5),
                    numParts = cursor.getIntOrNull(6)
                )
            )
        }
        cursor.close()
        db.close()
        return items
    }
}
