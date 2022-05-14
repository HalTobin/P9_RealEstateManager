package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.Flow

interface EstateRepository {

    fun getEstates(): Flow<List<Estate>>

    fun addEstate(estate: Estate)

}