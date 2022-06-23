package com.openclassrooms.realestatemanager.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateUI
import kotlinx.coroutines.flow.Flow

interface EstateRepository {

    fun getEstates(): Flow<List<EstateUI>>

    fun searchEstates(query: SimpleSQLiteQuery) : Flow<List<EstateUI>>

    fun getEstate(id: Int): Flow<EstateUI>

    suspend fun addEstate(estate: Estate): Long

    suspend fun changeSoldState(id: Int, sold: Boolean)

    suspend fun changeSoldDate(id: Int, soldDate: Long)

}