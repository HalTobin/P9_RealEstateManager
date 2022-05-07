package com.openclassrooms.realestatemanager.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.util.JavaFusedLocationProviderClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel(private val estateRepository: EstateRepository, private val coordinatesRepository: CoordinatesRepository) : ViewModel(), KoinComponent {

    //private val estateRepository: EstateRepository by inject()
    //private val coordinatesRepository: CoordinatesRepository by inject()

    /*@OptIn(DelicateCoroutinesApi::class)
    private val estates: Flow<List<Estate>> = flow {
        while(true) {
            estateRepository.getEstates().collect {
                it.forEach { myEstate ->

                    GlobalScope.launch(Dispatchers.IO) {
                        val response = coordinatesRepository.getCoordinates(myEstate.address)
                        if(response.isSuccessful) {
                            myEstate.xCoordinate = response.body()?.xCoordinate
                            myEstate.yCoordinate = response.body()?.yCoordinate
                        }
                    }

                    /*coordinatesRepository.getCoordinates(myEstate.address).collect { xy ->
                        myEstate.xCoordinate = xy.xCoordinate
                        myEstate.yCoordinate = xy.yCoordinate
                    }*/
                }
                emit(it)
            }
        }
    }*/
    private val estates: MutableLiveData<List<Estate>> by lazy { MutableLiveData<List<Estate>>() }
    private val coordinates: MutableLiveData<Coordinates> by lazy { MutableLiveData<Coordinates>() }

    //fun getEstates(): Flow<List<Estate>> { return estates }

    fun getEstates(): LiveData<List<Estate>> {
        val myList: ArrayList<Estate> = ArrayList()
        estateRepository.getEstates().asLiveData().value?.forEach { myEstate ->
            coordinatesRepository.getCoordinates(myEstate.address).asLiveData().value?.apply {
                myEstate.xCoordinate = xCoordinate
                myEstate.yCoordinate = yCoordinate
                myList.add(myEstate)
            }
        }
        estates.postValue(myList)

        /*estateRepository.getEstates().collect {
            it.forEach { myEstate ->

                /*val response = coordinatesRepository.getCoordinates(myEstate.address)
                if(response.isSuccessful) {
                    myEstate.xCoordinate = response.body()?.xCoordinate
                    myEstate.yCoordinate = response.body()?.yCoordinate
                }*/

                coordinatesRepository.getCoordinates(myEstate.address).collect { xy ->
                    xy?.apply {
                        myEstate.xCoordinate = xCoordinate
                        myEstate.yCoordinate = yCoordinate
                    }
                }
            }

            estates.value = it
        }*/

        return estates
    }

    private fun setLocation(xNewCoordinate: Double, yNewCoordinate: Double) {
        coordinates.postValue(Coordinates(xNewCoordinate, yNewCoordinate))
    }

    fun getLocation(): LiveData<Coordinates> {
        return coordinates
    }

    @SuppressLint("MissingPermission")
    fun findCurrentLocation(context: Context?) {
        val myLocationClient = JavaFusedLocationProviderClient(context)
        myLocationClient.currentLocation.addOnSuccessListener { location: Location? ->
            location?.apply { setLocation(latitude, longitude) }
        }
    }

    fun isPositionStackApiKeyDefined() = coordinatesRepository.isApiKeyDefined()

    fun setPositionStackApiKey(key: String) { coordinatesRepository.setApiKey(key) }
}