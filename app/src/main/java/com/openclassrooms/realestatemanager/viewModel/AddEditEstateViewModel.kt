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
import com.openclassrooms.realestatemanager.util.Utils.fullAddress
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class AddEditEstateViewModel(private val estateRepository: EstateRepository, private val coordinatesRepository: CoordinatesRepository) : ViewModel(), KoinComponent {

    //private val _estate = MutableLiveData<Estate>()
    //val estate = _estate

    private val _country = MutableLiveData<String>()
    val country = _country

    private val _city = MutableLiveData<String>()
    val city = _city

    private val _zip = MutableLiveData<String>()
    val zip = _zip

    private val _address = MutableLiveData<String>()
    val address = _address

    private val _coordinates = MutableLiveData<Coordinates>()
    val coordinates = _coordinates

    fun searchLocation() {
        viewModelScope.launch {
            if(getCurrentFullAddress() != null) {
                println("SEARCH LOCATION : " + getCurrentFullAddress())
                coordinatesRepository.getCoordinates(getCurrentFullAddress()!!).collect { coordinates ->
                    if(coordinates != null) setLocation(coordinates.xCoordinate, coordinates.yCoordinate)
                }
            }
        }
    }

    private fun setLocation(xNewCoordinate: Double, yNewCoordinate: Double) {
        _coordinates.postValue(Coordinates(xNewCoordinate, yNewCoordinate))
    }

    private fun getCurrentFullAddress(): String? {
        return if(_address.value != null && _city.value != null && _country.value != null) {
            fullAddress(_address.value!!, _zip.value, _city.value!!, _country.value!!)
        } else null
    }

    fun setCountry(country: String) { _country.value = country }

    fun setCity(city: String) { _city.value = city }

    fun setZip(zip: String) { _zip.value = zip }

    fun setAddress(address: String) { _address.value = address }

    fun isPositionStackApiKeyDefined() = coordinatesRepository.isApiKeyDefined()

    fun setPositionStackApiKey(key: String) { coordinatesRepository.setApiKey(key) }

}