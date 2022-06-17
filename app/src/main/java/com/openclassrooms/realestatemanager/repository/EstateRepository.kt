package com.openclassrooms.realestatemanager.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateWithImages
import kotlinx.coroutines.flow.Flow

interface EstateRepository {

    fun getEstates(): Flow<List<EstateWithImages>>

    fun searchEstates(query: SimpleSQLiteQuery) : Flow<List<EstateWithImages>>

    fun getEstate(id: Int): Flow<EstateWithImages>

    suspend fun addEstate(estate: Estate): Long

    suspend fun changeSoldState(id: Int, sold: Boolean)

    suspend fun changeSoldDate(id: Int, soldDate: Long)

}