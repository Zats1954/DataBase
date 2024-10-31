package ru.zatsoft.spinner

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "PRODUCT_DATABASE"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "product_table"
        val KEY_ID = "id"
        val KEY_NAME = "name"
        val KEY_WEIGHT = "weight"
        val KEY_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val queiry = ("CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_WEIGHT + " INTEGER, " +
                KEY_PRICE + " INTEGER " + ")")
        db.execSQL(queiry)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun addName(name: String, weight: Int, price: Int) {
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_WEIGHT, weight)
        values.put(KEY_PRICE, price)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getInfo(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

//    fun removeAll() {
//        val db = this.writableDatabase
//        db.delete(TABLE_NAME, null, null)
//    }

    fun removeProduct(id: Long): Boolean {
        try {
            val db = this.writableDatabase
            db.delete(TABLE_NAME, "$KEY_ID = $id", null)
            return true
        } catch (e: SQLException) {
            println("Ошибка DataBase удаление $id")
            return false
        }
    }

    fun update(product: Product?) {
        if (product != null) {
            val values = ContentValues()
            values.put(KEY_NAME, product.name)
            values.put(KEY_WEIGHT, product.weight)
            values.put(KEY_PRICE, product.price)
            val db = this.writableDatabase
            db.update(TABLE_NAME, values, "$KEY_ID = ${product.id}", arrayOf())
            db.close()
        }
    }
}