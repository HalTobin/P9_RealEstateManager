package com.openclassrooms.realestatemanager.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import com.openclassrooms.realestatemanager.util.Utils.fromEuroToDollar
import com.openclassrooms.realestatemanager.util.Utils.fullAddress
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddEditEstateViewModel(
    private val estateRepository: EstateRepository,
    private val imageRepository: ImageRepository,
    private val coordinatesRepository: CoordinatesRepository,
    private val agentRepository: AgentRepository
) : ViewModel() {

    private val _title = MutableLiveData("")
    val title = _title

    private val _type = MutableLiveData(0)
    val type = _type

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

    private val _price = MutableLiveData(0)
    val price = _price
    var priceAsDollar: Int = 0

    private val _isDollar = MutableLiveData(true)
    val isDollar = _isDollar

    private val _coordinates = MutableLiveData<Coordinates>()
    val coordinates = _coordinates

    private var picturesListToDelete = mutableListOf<ImageWithDescription>()
    private var pictureList = mutableListOf<ImageWithDescription>()
    private val _pictures = MutableLiveData<List<ImageWithDescription>>()
    val pictures = _pictures

    private val _nearbyPark = MutableLiveData(false)
    val nearbyPark = _nearbyPark

    private val _nearbyShop = MutableLiveData(false)
    val nearbyShop = _nearbyShop

    private val _nearbySchool = MutableLiveData(false)
    val nearbySchool = _nearbySchool

    private val _agent = MutableLiveData<Int>()
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

    private val _sold = MutableLiveData(false)
    val sold = _sold

    private val _warning = MutableLiveData<Int>()
    val warning = _warning

    private val _mustClose = MutableLiveData<Boolean>()
    val mustClose = _mustClose

    var currentEstateId: Int? = null

    // Return the list of agents
    fun getListOfAgent(): LiveData<List<Agent>> {
        return agentRepository.getAgents().asLiveData()
    }

    // Search the location of the estate
    fun searchLocation() {
        viewModelScope.launch {
            if (getCurrentFullAddress() != null) {
                Log.i("SEARCH LOCATION", getCurrentFullAddress().toString())
                val myCoordinates =
                    coordinatesRepository.getCoordinates(getCurrentFullAddress()!!).first()
                if (myCoordinates != null) setLocation(
                    myCoordinates.xCoordinate,
                    myCoordinates.yCoordinate
                )
            }
        }
    }

    // Load an estate if an estateId has been provided
    fun loadEstate(estateId: Int) {
        viewModelScope.launch {
            estateRepository.getEstate(estateId).collect {
                currentEstateId = it.estate.id
                _title.postValue(it.estate.title)
                _type.postValue(it.estate.type)
                _country.postValue(it.estate.country)
                _city.postValue(it.estate.city)
                _zip.postValue(it.estate.zipCode)
                _address.postValue(it.estate.address)
                _area.postValue(it.estate.area!!)
                _price.postValue(it.estate.priceDollar!!)
                priceAsDollar = it.estate.priceDollar!!
                _isDollar.postValue(true)
                _coordinates.postValue(
                    Coordinates(
                        it.estate.xCoordinate!!,
                        it.estate.yCoordinate!!
                    )
                )
                pictureList = it.images as MutableList<ImageWithDescription>
                _pictures.postValue(pictureList)
                _nearbyPark.postValue(it.estate.nearbyPark!!)
                _nearbyShop.postValue(it.estate.nearbyShop!!)
                _nearbySchool.postValue(it.estate.nearbySchool!!)
                _agent.postValue(it.estate.agentId)
                _description.postValue(it.estate.description)
                _nbRooms.postValue(it.estate.nbRooms!!)
                _nbBathrooms.postValue(it.estate.nbBathrooms!!)
                _nbBedrooms.postValue(it.estate.nbBedrooms!!)
                _entryDate.postValue(it.estate.entryDate)
                _sold.postValue(it.estate.sold)
                if (it.estate.soldDate != null) _soldDate.value =
                    it.estate.soldDate!! else _soldDate.value = 0
            }
        }
    }

    // Define the coordinates of the estate
    fun setLocation(xNewCoordinate: Double, yNewCoordinate: Double) {
        _coordinates.postValue(Coordinates(xNewCoordinate, yNewCoordinate))
    }

    // Allows the user to change currency from EURO to DOLLAR or from DOLLAR to EURO
    fun changeCurrency() {
        priceAsDollar =
            if (isDollar.value!!) _price.value?.fromEuroToDollar()!! else _price.value!!
        _isDollar.postValue(!_isDollar.value!!)
        Log.i("PRICE AS DOLLAR", priceAsDollar.toString())
    }

    // Get the full address of an estate
    private fun getCurrentFullAddress(): String? {
        return if (_address.value != null && _city.value != null && _country.value != null) {
            fullAddress(_address.value!!, _zip.value, _city.value!!, _country.value!!)
        } else null
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    fun setType(type: Int) {
        _type.value = type
    }

    fun setCountry(country: String) {
        _country.value = country
    }

    fun setCity(city: String) {
        _city.value = city
    }

    fun setZip(zip: String) {
        _zip.value = zip
    }

    fun setAddress(address: String) {
        _address.value = address
    }

    fun setArea(area: String) {
        _area.value = area.toInt()
    }

    fun setPrice(price: String) {
        _price.value = price.toInt()
        priceAsDollar =
            if (!isDollar.value!!) price.toInt().fromEuroToDollar() else price.toInt()
        Log.i("PRICE AS DOLLAR", priceAsDollar.toString())
    }

    fun setRooms(rooms: String) {
        _nbRooms.value = rooms.toInt()
    }

    fun setBedrooms(bedrooms: String) {
        _nbBedrooms.value = bedrooms.toInt()
    }

    fun setBathrooms(bathrooms: String) {
        _nbBathrooms.value = bathrooms.toInt()
    }

    fun setPark(park: Boolean) {
        _nearbyPark.value = park
    }

    fun setSchool(school: Boolean) {
        _nearbySchool.value = school
    }

    fun setShop(shop: Boolean) {
        _nearbyShop.value = shop
    }

    fun setAgent(agentId: Int) {
        _agent.value = agentId
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    // Add a picture to the list of ImageWithDescription associated to the estate
    fun addPicture(image: String, text: String) {
        if (currentEstateId == null) pictureList.add(
            ImageWithDescription(
                estateId = -1,
                description = text,
                imageUrl = image
            )
        )
        else pictureList.add(
            ImageWithDescription(
                estateId = currentEstateId!!,
                description = text,
                imageUrl = image
            )
        )
        _pictures.postValue(pictureList)
    }

    // Remove a picture to the list of ImageWithDescription associated to the estate
    fun removePicture(imageWithDescription: ImageWithDescription) {
        if(imageWithDescription.id != null) picturesListToDelete.add(imageWithDescription)
        pictureList.remove(imageWithDescription)
        _pictures.postValue(pictureList)
    }

    // Save the estate to the database
    fun saveEstate() {
        // Check if all necessary fields are correctly filled
        if (Estate.isFilled(
                _title.value,
                _address.value,
                _city.value,
                _country.value,
                _coordinates.value,
                priceAsDollar,
                _area.value,
                _nbRooms.value,
                _nbBathrooms.value,
                _nbBedrooms.value,
                _agent.value,
                _description.value
            )
        ) {
            // The value '_entryDate' is set only if this is a new estate
            // If this is an edited estate, then the value doesn't change
            if (_entryDate.value == null) _entryDate.value = System.currentTimeMillis()
            viewModelScope.launch {
                currentEstateId = estateRepository.addEstate(
                    Estate(
                        id = currentEstateId,
                        type = _type.value!!,
                        title = _title.value!!,
                        address = _address.value!!,
                        city = _city.value!!,
                        country = _country.value!!,
                        zipCode = _zip.value!!,
                        xCoordinate = _coordinates.value!!.xCoordinate,
                        yCoordinate = _coordinates.value!!.yCoordinate,
                        priceDollar = priceAsDollar,
                        area = _area.value!!,
                        nbRooms = _nbRooms.value!!,
                        nbBathrooms = _nbBathrooms.value!!,
                        nbBedrooms = _nbBedrooms.value!!,
                        nearbySchool = _nearbySchool.value,
                        nearbyShop = _nearbyShop.value,
                        nearbyPark = _nearbyPark.value,
                        sold = _sold.value!!,
                        entryDate = _entryDate.value,
                        soldDate = _soldDate.value,
                        agentId = _agent.value!!,
                        description = _description.value!!
                    )
                ).toInt()

                // Assigned the non-assigned ImageWithDescription from picturesList object to the current estate
                for (imageWithDescription in pictureList) {
                    if (imageWithDescription.estateId == -1) imageWithDescription.estateId =
                        currentEstateId!!
                }

                // Assigned the non-assigned ImageWithDescription from picturesListToDelete object to the current estate
                for (imageWithDescription in picturesListToDelete) {
                    if (imageWithDescription.estateId == -1) imageWithDescription.estateId =
                        currentEstateId!!
                }

                // Delete the ImageWithDescription in the picturesListToDelete from the database
                imageRepository.deleteListOfImages(picturesListToDelete)

                // Save the ImageWithDescription list into the database
                _pictures.value?.let {
                    imageRepository.addListOfImages(it)
                }

                // Indicate to the UI that the activity should closed
                _mustClose.postValue(true)
            }

        } else _warning.postValue(Estate.UNCOMPLETED)
        if (_coordinates.value == null) _warning.postValue(Estate.CANT_FIND_LOCATION)
    }

}