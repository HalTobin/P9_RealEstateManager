package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Coordinates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PositionStackApi {

    // Query to get coordinates from an address
    @GET("forward")
    suspend fun getCoordinates(
        @Query("access_key") apiKey: String?,
        @Query("query") address: String?,
        @Query("limit") limit: Int? = 1
    ): Response<Coordinates?>

}