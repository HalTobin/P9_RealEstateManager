package com.openclassrooms.realestatemanager.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.api.PositionStackApi
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.*

class CoordinatesRepositoryImpl(private val positionStackApi: PositionStackApi) : CoordinatesRepository {

    private var apiKey: String? = null

    override fun getCoordinates(address: String): LiveData<Coordinates?> {

        val estateCoordinates: MutableLiveData<Coordinates> = MutableLiveData<Coordinates>()
        var coordinates: Flow<Coordinates?> = flowOf(Coordinates())

        var nonLiveCoordinates = Coordinates()

        println("GET COORDINATES - " + address)

        if(isApiKeyDefined()) {
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

                        nonLiveCoordinates = response.body()!!

                        estateCoordinates.value = response.body()
                        estateCoordinates.postValue(response.body())

                        coordinates = flow {
                            emit(response.body())
                        }

                    }
                }
                override fun onFailure(call: Call<Coordinates?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
         return estateCoordinates
    }

    override fun setApiKey(key: String) { apiKey = key }

    override fun isApiKeyDefined(): Boolean = (apiKey!=null)
}