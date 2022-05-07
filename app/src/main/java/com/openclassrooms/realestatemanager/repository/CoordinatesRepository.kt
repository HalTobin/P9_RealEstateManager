package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.Coordinates
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CoordinatesRepository {

    fun getCoordinates(address: String): LiveData<Coordinates?>

    fun setApiKey(key: String)

    fun isApiKeyDefined(): Boolean

}