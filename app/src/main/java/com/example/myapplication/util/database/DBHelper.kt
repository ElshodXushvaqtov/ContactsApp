package com.example.myapplication.util.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.util.module.Contact

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "ContactDatabase"
        private val TABLE_CONTACTS = "ContactTable"
        private val ID = "id"
        private val NAME = "name"
        private val NUMBER = "phone_number"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT,"
                + NUMBER + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun addContact(contact: Contact): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, contact.id)
        contentValues.put(NAME, contact.name)
        contentValues.put(NUMBER, contact.phone_number)
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    fun deleteEmployee(contact: Contact): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, contact.id)

        val success = db.delete(TABLE_CONTACTS, "id=" + contact.id, null)
        db.close()
        return success
    }

    fun updateEmployee(contact: Contact): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, contact.id)
        contentValues.put(NAME, contact.name)
        contentValues.put(NUMBER, contact.phone_number)

        val success = db.update(TABLE_CONTACTS, contentValues, "id=" + contact.id, null)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewContact(): List<Contact> {
        val contactList: ArrayList<Contact> = ArrayList<Contact>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var contactId: Int
        var contactName: String
        var contactNumber: String
        if (cursor.moveToFirst()) {
            do {
                contactId = cursor.getInt(cursor.getColumnIndex("id"))
                contactName = cursor.getString(cursor.getColumnIndex("name"))
                contactNumber = cursor.getString(cursor.getColumnIndex("email"))
                val emp = Contact(id = contactId, name = contactName, phone_number = contactNumber)
                contactList.add(emp)
            } while (cursor.moveToNext())
        }
        return contactList
    }
}