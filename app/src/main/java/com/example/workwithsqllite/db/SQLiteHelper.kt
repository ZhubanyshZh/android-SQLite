package com.example.workwithsqllite.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.workwithsqllite.Purchase
import java.io.Serializable

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, "purchase", null, 1),
    Serializable {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE purchase(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT," +
                " amount TEXT )"
        db?.execSQL(createTable)
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

        val success = db.insert("purchase", null, values)
        db.close()
        return success
    }

    fun getAllPurchase(): ArrayList<Purchase> {
        val purchaseList: ArrayList<Purchase> = ArrayList()
        val selectQuery = "SELECT * FROM purchase"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

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

        val success = db.update("purchase", values, "id=?", arrayOf(id.toString()))
        db.close()
        return success
    }

    fun deletePurchase(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete("purchase", "id=?", arrayOf(id.toString()))
        db.close()
        return success
    }

    fun clear() {
        val db = this.writableDatabase

        val clearAll = """
            DELETE FROM purchase
        """.trimIndent()
        db?.execSQL(clearAll)
    }
}