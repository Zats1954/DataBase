package ru.zatsoft.spinner

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
SQLiteOpenHelper(context, DATABASE_NAME,factory, DATABASE_VERSION){
    companion object{
        private val DATABASE_NAME = "PERSON_DATABASE"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "person_table"
        val KEY_ID = "id"
        val KEY_NAME = "name"
        val KEY_LAST_NAME = "lastName"
        val KEY_AGE = "age"
        val KEY_POSITION = "position"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
//                KEY_NAME + " TEXT, " +
//                KEY_LAST_NAME + " TEXT, " +
//                KEY_AGE + " TEXT, " +
                KEY_POSITION + " TEXT" +")")
        println("---- database open ${db?.isOpen} ")
        db?.execSQL(query)
    }

    fun showTables(){
        val db = this.readableDatabase
          db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table';", null)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun addName(name: String, lastName: String,  age: String,  position: String){
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_LAST_NAME,lastName)
        values.put(KEY_AGE,age)
        values.put(KEY_POSITION, position)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getInfo(): Cursor?{
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun removeAll(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    fun clear() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }
}