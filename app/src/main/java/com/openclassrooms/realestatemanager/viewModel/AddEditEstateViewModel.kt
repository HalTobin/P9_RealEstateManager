package com.openclassrooms.realestatemanager.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.util.CustomTakePicture
import com.openclassrooms.realestatemanager.util.Utils.fullAddress
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddEditEstateViewModel(private val estateRepository: EstateRepository, private val coordinatesRepository: CoordinatesRepository) : ViewModel(), KoinComponent {

    private val _country = MutableLiveData<String>()
    val country = _country

    private val _city = MutableLiveData<String>()
    val city = _city

    private val _zip = MutableLiveData<String>()
    val zip = _zip

    private val _address = MutableLiveData<String>()
    val address = _address

    private val _isDollar = MutableLiveData<Boolean>()
    val isDollar = _isDollar

    private val _coordinates = MutableLiveData<Coordinates>()
    val coordinates = _coordinates

    private val pictureList = mutableListOf<ImageWithDescription>()
    private val _pictures = MutableLiveData<List<ImageWithDescription>>()
    val pictures = _pictures

    init {
        _isDollar.value = true
    }

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

    fun changeCurrency() {
        if(_isDollar.value != null) _isDollar.postValue(!_isDollar.value!!)
        else _isDollar.postValue(true)
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

    fun addPicture(image: String) {
        pictureList.add(ImageWithDescription(1, 1, "Test", image))
        _pictures.postValue(pictureList)
    }

    fun removePicture(imageWithDescription: ImageWithDescription) {
        pictureList.remove(imageWithDescription)
        _pictures.postValue(pictureList)
    }

    fun isPositionStackApiKeyDefined() = coordinatesRepository.isApiKeyDefined()

    fun setPositionStackApiKey(key: String) { coordinatesRepository.setApiKey(key) }

}