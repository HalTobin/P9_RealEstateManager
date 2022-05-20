package com.openclassrooms.realestatemanager.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import com.openclassrooms.realestatemanager.util.CustomTakePicture
import com.openclassrooms.realestatemanager.util.Utils.fromEuroToDollar
import com.openclassrooms.realestatemanager.util.Utils.fullAddress
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddEditEstateViewModel(private val estateRepository: EstateRepository, private val imageRepository: ImageRepository, private val coordinatesRepository: CoordinatesRepository) : ViewModel(), KoinComponent {

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
    var priceAsDollar: Int = 0

    private val _isDollar = MutableLiveData(true)
    val isDollar = _isDollar

    private val _coordinates = MutableLiveData<Coordinates>()
    val coordinates = _coordinates

    private var pictureList = mutableListOf<ImageWithDescription>()
    private val _pictures = MutableLiveData<List<ImageWithDescription>>()
    val pictures = _pictures

    private val _nearbyPark = MutableLiveData(false)
    val nearbyPark = _nearbyPark

    private val _nearbyShop = MutableLiveData(false)
    val nearbyShop = _nearbyShop

    private val _nearbySchool = MutableLiveData(false)
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

    private val _entryDate = MutableLiveData(System.currentTimeMillis())

    private val _soldDate = MutableLiveData<Long>()

    private val _status = MutableLiveData(Estate.AVAILABLE)
    val status = _status

    private val _warning = MutableLiveData<Int>()
    val warning = _warning

    private val _mustClose = MutableLiveData<Boolean>()
    val mustClose = _mustClose

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

    fun loadEstate(estateId: Int) {
        viewModelScope.launch {
            estateRepository.getEstate(estateId).collect {
                _title.postValue(it.estate.title)
                _country.postValue(it.estate.country)
                _city.postValue(it.estate.city)
                _zip.postValue(it.estate.zipCode)
                _address.postValue(it.estate.address)
                _area.postValue(it.estate.area!!)
                _price.postValue(it.estate.priceDollar!!)
                _isDollar.postValue(true)
                _coordinates.postValue(Coordinates(it.estate.xCoordinate!!, it.estate.yCoordinate!!))
                pictureList = it.images as MutableList<ImageWithDescription>
                _pictures.postValue(pictureList)
                _nearbyPark.postValue(it.estate.nearbyPark!!)
                _nearbyShop.postValue(it.estate.nearbyShop!!)
                _nearbySchool.postValue(it.estate.nearbySchool!!)
                _agent.postValue(it.estate.agent)
                _description.postValue(it.estate.description)
                _nbRooms.postValue(it.estate.nbRooms!!)
                _nbBathrooms.postValue(it.estate.nbBathrooms!!)
                _nbBedrooms.postValue(it.estate.nbBedrooms!!)
                _entryDate.value = it.estate.entryDate
                if(it.estate.soldDate != null) _soldDate.value = it.estate.soldDate!! else _soldDate.value = 0
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

    fun setTitle(title: String) { _title.value = title }

    fun setCountry(country: String) { _country.value = country }

    fun setCity(city: String) { _city.value = city }

    fun setZip(zip: String) { _zip.value = zip }

    fun setAddress(address: String) { _address.value = address }

    fun setArea(area: String) { _area.value = area.toInt() }

    fun setPrice(price: String) {
        _price.value = price.toInt()
        priceAsDollar = if(!isDollar.value!!) price.toInt().fromEuroToDollar() else price.toInt()
    }

    fun setRooms(rooms: String) { _nbRooms.value = rooms.toInt() }

    fun setBedrooms(bedrooms: String) { _nbBedrooms.value = bedrooms.toInt() }

    fun setBathrooms(bathrooms: String) { _nbBathrooms.value = bathrooms.toInt() }

    fun setPark(park: Boolean) { _nearbyPark.value = park }

    fun setSchool(school: Boolean) { _nearbySchool.value = school }

    fun setShop(shop: Boolean) { _nearbyShop.value = shop }

    fun setAgent(agent: String) { _agent.value = agent }

    fun setDescription(description: String) { _description.value = description }

    fun addPicture(image: String, text: String) {
        pictureList.add(ImageWithDescription(1, 1, text, image))
        _pictures.postValue(pictureList)
    }

    fun removePicture(imageWithDescription: ImageWithDescription) {
        File(imageWithDescription.imageUrl).delete()
        pictureList.remove(imageWithDescription)
        _pictures.postValue(pictureList)
    }

    fun saveEstate() {
        if(Estate.isFilled(_title.value, _address.value, _city.value, _country.value, _coordinates.value, priceAsDollar, _area.value, _nbRooms.value, _nbBathrooms.value, _nbBedrooms.value, _agent.value, _description.value)) {
            viewModelScope.launch {
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
                    //pictures = _pictures.value,
                    nearbySchool = _nearbySchool.value,
                    nearbyShop = _nearbyShop.value,
                    nearbyPark = _nearbyPark.value,
                    status = _status.value,
                    entryDate = _entryDate.value,
                    soldDate = _soldDate.value,
                    agent = _agent.value!!,
                    description = _description.value!!))
                _mustClose.postValue(true)

                _pictures.value?.let { imageRepository.addListOfImages(it) }

            }

        }
        if(_coordinates.value == null) _warning.postValue(Estate.CANT_FIND_LOCATION)
        else if(coordinates.value!!.isUndefined()) _warning.postValue(Estate.CANT_FIND_LOCATION)
        else _warning.postValue(Estate.UNCOMPLETE)
    }

    fun isPositionStackApiKeyDefined() = coordinatesRepository.isApiKeyDefined()

    fun setPositionStackApiKey(key: String) { coordinatesRepository.setApiKey(key) }

}