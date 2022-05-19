package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.Flow

interface EstateRepository {

    fun getEstates(): Flow<List<Estate>>

    fun getEstate(id: Long): Flow<Estate>

    fun addEstate(estate: Estate)

}