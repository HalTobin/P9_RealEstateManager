package com.openclassrooms.realestatemanager.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Environment
import androidx.lifecycle.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateUI
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import kotlinx.coroutines.launch
import java.io.File


class MainViewModel(private val estateRepository: EstateRepository, private val imageRepository: ImageRepository) : ViewModel() {

    private var estateId: Int? = null

    private val _estate = MutableLiveData<EstateUI>()
    val estate = _estate

    private val _estates = MediatorLiveData<List<EstateUI>>()
    val estates = _estates

    private val _coordinates = MutableLiveData<Coordinates>()
    val coordinates = _coordinates

    private val _selection = MutableLiveData<Int>()
    val selection = _selection

    private val _closeDetails = MutableLiveData(false)
    val closeDetails = _closeDetails

    private val _isSearchInDollar = MutableLiveData(true)
    val isSearchInDollar = _isSearchInDollar

    private val searchEstate = Estate()
    private var search = false

    init {
        _estates.addSource(estateRepository.getEstates().asLiveData()) { value ->
            _estates.setValue(
                value
            )
        }
    }

    fun getImages(): LiveData<List<ImageWithDescription>> {
        return imageRepository.getImages().asLiveData()
    }

    // Check if images and videos are used in the database, if they aren't the file is deleted
    fun cleanImageFolder(context: Context, images: List<ImageWithDescription>) {
        val path = context.filesDir.toPath().toString() + "/Images"
        val directory = File(path)
        val files = directory.listFiles()

        files?.forEach { file ->
            var exist = false

            images.forEach { media ->
                if(file.path.equals(media.imageUrl)) exist = true
            }

            if(!exist) file.delete()

        }

    }

    private fun setLocation(xNewCoordinate: Double, yNewCoordinate: Double) {
        _coordinates.postValue(Coordinates(xNewCoordinate, yNewCoordinate))
    }

    @SuppressLint("MissingPermission")
    fun findCurrentLocation(context: Context) {
        val myLocationClient = LocationServices.getFusedLocationProviderClient(context)

        myLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                location?.apply { setLocation(latitude, longitude) }
            }
    }

    fun selectEstate(estateId: Int) {
        _selection.postValue(estateId)
    }

    fun setEstateId(estateId: Int) {
        this.estateId = estateId
        viewModelScope.launch {
            estateRepository.getEstate(estateId).collect {
                _estate.postValue(it)
            }
        }
    }

    fun updateSoldState() {
        viewModelScope.launch {
            estateRepository.changeSoldState(
                _estate.value!!.estate.id!!,
                !_estate.value!!.estate.sold!!
            )
            estateRepository.changeSoldDate(_estate.value!!.estate.id!!, System.currentTimeMillis())
        }
    }

    //TODO - Add min price, date, and sold state
    fun startSearch() {
        search = true
        _estates.addSource(
            estateRepository.searchEstates(searchEstate.getRequest()).asLiveData()
        ) { value -> _estates.setValue(value) }
        _estates.removeSource(estateRepository.getEstates().asLiveData())
    }

    fun stopSearch() {
        if (search) {
            search = false
            _estates.addSource(
                estateRepository.getEstates().asLiveData()
            ) { value -> _estates.setValue(value) }
            _estates.removeSource(
                estateRepository.searchEstates(searchEstate.getRequest()).asLiveData()
            )
        }
    }

    fun setSearch(value: String, field: Int) {
        when (field) {
            Estate.CITY -> searchEstate.city = value
            Estate.ZIPCODE -> searchEstate.zipCode = value
            Estate.COUNTRY -> searchEstate.country = value
        }
    }

    fun setSearch(value: Int, field: Int) {
        when (field) {
            Estate.TYPE -> searchEstate.type = value
            Estate.PRICE -> searchEstate.priceDollar = value
            Estate.AREA -> searchEstate.area = value
            Estate.ROOMS -> searchEstate.nbRooms = value
            Estate.BEDROOMS -> searchEstate.nbBedrooms = value
            Estate.BATHROOMS -> searchEstate.nbBathrooms = value
            Estate.AGENT -> searchEstate.agentId = value
        }
    }

    fun setSearch(value: Boolean, field: Int) {
        when (field) {
            Estate.SCHOOL -> searchEstate.nearbySchool = value
            Estate.SHOP -> searchEstate.nearbyShop = value
            Estate.PARK -> searchEstate.nearbyPark = value
        }
    }

    fun invertCurrency() {
        _isSearchInDollar.value = !_isSearchInDollar.value!!
    }

}