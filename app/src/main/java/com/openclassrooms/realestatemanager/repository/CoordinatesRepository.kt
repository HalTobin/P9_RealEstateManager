package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.model.Coordinates
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CoordinatesRepository {

    fun getCoordinates(address: String): Flow<Coordinates?>

    fun setApiKey(key: String)

    fun isApiKeyDefined(): Boolean

}