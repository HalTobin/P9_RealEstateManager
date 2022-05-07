package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.api.PositionStackApi
import com.openclassrooms.realestatemanager.api.RetrofitClient
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import retrofit2.*

class CoordinatesRepositoryImpl(private val positionStackApi: PositionStackApi) : CoordinatesRepository {

    private var apiKey: String? = null

    override fun getCoordinates(address: String): Flow<Coordinates?> {
        var coordinates: Flow<Coordinates?> = flowOf(Coordinates())
        if(apiKey != null) {
            //val response = positionStackApi.getCoordinates(apiKey, address, 1).await()

            /*if(response.isSuccessful) {
                coordinates = flowOf(response.body())
            }*/

            /*GlobalScope.launch(Dispatchers.IO) {
                coordinates = flowOf()
            }*/

            positionStackApi.getCoordinates(apiKey, address, 1).enqueue(object : Callback<Coordinates?> {
                override fun onResponse(call: Call<Coordinates?>, response: Response<Coordinates?>) {
                    response.body()?.apply {
                        coordinates = flowOf(this)
                    }
                }
                override fun onFailure(call: Call<Coordinates?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
         return coordinates
    }

    override fun setApiKey(key: String) { apiKey = key }

    override fun isApiKeyDefined(): Boolean = (apiKey!=null)
}