package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.model.Coordinates
import kotlinx.coroutines.flow.Flow

interface CoordinatesRepository {

    suspend fun getCoordinates(address: String): Flow<Coordinates?>

}