package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.Coordinates
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CoordinatesRepository {

    suspend fun getCoordinates(address: String): Flow<Coordinates?>

}