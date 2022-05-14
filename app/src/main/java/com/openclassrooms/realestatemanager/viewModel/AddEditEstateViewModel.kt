package com.openclassrooms.realestatemanager.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
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

    private val _title = MutableLiveData("")
    val title = _title

    private val _country = MutableLiveData("")
    val country = _country

    private val _city = MutableLiveData("")
    val city = _city

    private val _zip = MutableLiveData("")
    val zip = _zip

    private val _address = MutableLiveData("")
    val address = _address

    private val _area = MutableLiveData<Int>()
    val area = _area

    private val _price = MutableLiveData<Int>()
    val price = _price

    private val _isDollar = MutableLiveData<Boolean>()
    val isDollar = _isDollar

    private val _coordinates = MutableLiveData<Coordinates>()
    val coordinates = _coordinates

    private val pictureList = mutableListOf<ImageWithDescription>()
    private val _pictures = MutableLiveData<List<ImageWithDescription>>()
    val pictures = _pictures

    private val _nearbyPark = MutableLiveData<Boolean>()
    val nearbyPark = _nearbyPark

    private val _nearbyShop = MutableLiveData<Boolean>()
    val nearbyShop = _nearbyShop

    private val _nearbySchool = MutableLiveData<Boolean>()
    val nearbySchool = _nearbySchool

    private val _agent = MutableLiveData("")
    val agent = _agent

    private val _description = MutableLiveData("")
    val description = _description

    private val _nbRooms = MutableLiveData<Int>()
    val nbRooms = _nbRooms

    private val _nbBathrooms = MutableLiveData<Int>()
    val nbBathrooms = _nbBathrooms

    private val _nbBedrooms = MutableLiveData<Int>()
    val nbBedrooms = _nbBedrooms

    private val _entryDate = MutableLiveData<Long>()
    val entryDate = _entryDate

    private val _soldDate = MutableLiveData<Long>()
    val soldDate = _soldDate

    private val _status = MutableLiveData<Int>(Estate.AVAILABLE)
    val status = _status

    init {
        _isDollar.value = true
        _description.value = "This is not empty..."
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

    fun saveEstate() {
        if(Estate.isFilled(_title.value, _address.value, _city.value, _country.value, _coordinates.value, _price.value, _area.value, _nbRooms.value, _nbBathrooms.value, _nbBedrooms.value, _soldDate.value, _agent.value, _description.value)) {
            estateRepository.addEstate(Estate(title = _title.value!!,
                    address = _address.value!!,
                    city = _city.value!!,
                    country = _country.value!!, zipCode = _zip.value!!,
                    xCoordinate = _coordinates.value!!.xCoordinate,
                    yCoordinate = _coordinates.value!!.yCoordinate, priceDollar = _price.value!!,
                    area = _area.value!!,
                    nbRooms = _nbRooms.value!!,
                    nbBathrooms = _nbBathrooms.value!!,
                    nbBedrooms = _nbBedrooms.value!!,
                    pictures = _pictures.value,
                    nearbySchool = _nearbySchool.value,
                    nearbyShop = _nearbyShop.value,
                    nearbyPark = _nearbyPark.value,
                    status = _status.value,
                    entryDate = System.currentTimeMillis(),
                    soldDate = 0,
                    agent = _agent.value!!,
                    description = _description.value!!))
        }

    }

    fun isPositionStackApiKeyDefined() = coordinatesRepository.isApiKeyDefined()

    fun setPositionStackApiKey(key: String) { coordinatesRepository.setApiKey(key) }

}