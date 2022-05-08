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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel(private val estateRepository: EstateRepository, private val coordinatesRepository: CoordinatesRepository) : ViewModel(), KoinComponent {

    //private val estateRepository: EstateRepository by inject()
    //private val coordinatesRepository: CoordinatesRepository by inject()

    private val _estates = MutableLiveData<List<Estate>>()
    val estates = _estates

    private val _coordinates = MutableLiveData<Coordinates>()
    val coordinates = _coordinates

    init {

        viewModelScope.launch {
            getEstates()
        }

    }

    private suspend fun getEstates() {

        estateRepository.getEstates().collect { list ->
            list.forEach { myEstate ->
                coordinatesRepository.getCoordinates(myEstate.address).collect { xy ->
                    xy?.apply {
                        myEstate.xCoordinate = xCoordinate
                        myEstate.yCoordinate = yCoordinate
                        _estates.value = list
                        _estates.postValue(list)
                    }
                }
            }

            //_estates.value = list
            //_estates.postValue(list)
        }

        //return _estates
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