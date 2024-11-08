package com.example.workwithsqllite.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.workwithsqllite.Purchase
import java.io.Serializable

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, "purchase", null, 1), Serializable {
    private val TABLE_NAME : String = "purchase"
    private val CREATE_DATABASE_ENTRIES : String = """
        CREATE TABLE $TABLE_NAME(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                amount TEXT)
    """.trimIndent()
    private val DELETE_ALL_FROM_DB_ENTRIES : String = """
        DELETE FROM $TABLE_NAME
    """.trimIndent()
    private val SELECT_ALL_QUERY : String = """
        SELECT * FROM $TABLE_NAME
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_DATABASE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS purchase")
        onCreate(db)
    }

    fun addPurchase(name: String, amount: Int): Long {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("amount", amount)

        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return success
    }

    fun getAllPurchase(): ArrayList<Purchase> {
        val purchaseList: ArrayList<Purchase> = ArrayList()
        val db = this.readableDatabase
        val cursor = db.rawQuery(SELECT_ALL_QUERY, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"))
                purchaseList.add(Purchase(id, name, amount))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return purchaseList
    }

    fun updatePurchase(id: Int, name: String, amount: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("amount", amount)

        val success = db.update(TABLE_NAME, values, "id=?", arrayOf(id.toString()))
        db.close()
        return success
    }

    fun deletePurchase(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "id=?", arrayOf(id.toString()))
        db.close()
        return success
    }

    fun clear() {
        val db = this.writableDatabase
        db?.execSQL(DELETE_ALL_FROM_DB_ENTRIES)
        db.close()
    }
}