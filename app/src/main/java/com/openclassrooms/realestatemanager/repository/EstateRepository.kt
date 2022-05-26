package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateWithImages
import kotlinx.coroutines.flow.Flow

interface EstateRepository {

    fun getEstates(): Flow<List<EstateWithImages>>

    fun getEstate(id: Int): Flow<EstateWithImages>

    suspend fun addEstate(estate: Estate): Long

    suspend fun changeSoldState(id: Int, sold: Boolean)

}