package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.data.EstateDatabase
import com.openclassrooms.realestatemanager.model.Estate
import org.koin.android.ext.android.inject

class EstateContentProvider :ContentProvider() {

    private val database: EstateDatabase by inject()

    companion object {
        const val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
        val TABLE_NAME = Estate::class.simpleName
        val URI_ITEM = Uri.parse("content://".plus(AUTHORITY).plus("/").plus(TABLE_NAME))
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder4: String?): Cursor? {
        if (context != null) {
            val id = ContentUris.parseId(uri).toInt()
            val cursor: Cursor =
                database.estateDao.getEstateWithCursor(id)
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(p0: Uri): String {
        return "vnd.android.cursor.estate/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri {
        if (context != null && contentValues != null) {
            val id: Long = database.estateDao.insert(Estate.fromContentValues(contentValues))
            if (id != 0L) {
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }
        throw java.lang.IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String?>?): Int {
        throw java.lang.IllegalArgumentException("Delete an estate isn't authorized $uri")
    }


    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String?>?): Int {
        if (context != null && contentValues != null) {
            val count: Int = database.estateDao
                .updateEstate(Estate.fromContentValues(contentValues))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw java.lang.IllegalArgumentException("Failed to update row into $uri")
    }

}