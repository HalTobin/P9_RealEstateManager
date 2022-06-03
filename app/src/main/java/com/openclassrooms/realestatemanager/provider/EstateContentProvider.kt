package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.model.Estate
import org.koin.core.KoinComponent

class EstateContentProvider { /*:ContentProvider() {

    //var estateRepository = inject<EstateRepositoryImpl>()

    val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
    val TABLE_NAME = Estate::class.simpleName
    val URI_ITEM = Uri.parse("content://".plus(AUTHORITY).plus("/").plus(TABLE_NAME))

    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder4: String?): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }*/

}