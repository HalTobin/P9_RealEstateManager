package com.openclassrooms.realestatemanager.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Environment
import androidx.lifecycle.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.openclassrooms.realestatemanager.model.*
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import kotlinx.coroutines.launch
import java.io.File


class MainViewModel(
    private val estateRepository: EstateRepository,
    private val imageRepository: ImageRepository,
    private val agentRepository: AgentRepository
) : ViewModel() {

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

    private var searchEstate = EstateSearch()
    val searchEstateLiveData = MutableLiveData<EstateSearch>()
    private var search = false

    init {
        _estates.addSource(estateRepository.getEstates().asLiveData()) { value ->
            _estates.setValue(
                value
            )
        }
    }

    fun getListOfAgent(): LiveData<List<Agent>> {
        return agentRepository.getAgents().asLiveData()
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
                if (file.path.equals(media.imageUrl)) exist = true
            }

            if (!exist) file.delete()

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

    fun startSearch() {
        search = true
        _estates.addSource(
            estateRepository.searchEstates(searchEstate.getRequest()).asLiveData()
        ) { value -> _estates.setValue(value) }
        _estates.removeSource(estateRepository.getEstates().asLiveData())
    }

    fun stopSearch() {
        setSearchNull(EstateSearch.ALL)
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
            EstateSearch.CITY -> searchEstate.city = value
            EstateSearch.ZIPCODE -> searchEstate.zipCode = value
            EstateSearch.COUNTRY -> searchEstate.country = value
        }
    }

    fun setSearch(value: Int, field: Int) {
        when (field) {
            EstateSearch.TYPE -> searchEstate.type = value
            EstateSearch.PRICE_MIN -> searchEstate.priceMinDollar = value
            EstateSearch.PRICE_MAX -> searchEstate.priceMaxDollar = value
            EstateSearch.AREA_MIN -> searchEstate.areaMin = value
            EstateSearch.AREA_MAX -> searchEstate.areaMax = value
            EstateSearch.ROOMS -> searchEstate.nbRooms = value
            EstateSearch.BEDROOMS -> searchEstate.nbBedrooms = value
            EstateSearch.BATHROOMS -> searchEstate.nbBathrooms = value
            EstateSearch.AGENT -> searchEstate.agentId = value
            EstateSearch.IMAGES -> searchEstate.nbImages = value
        }
    }

    fun setSearch(value: Long, field: Int) {
        when (field) {
            EstateSearch.IN_SALE_SINCE -> searchEstate.entryDate = value
            EstateSearch.SOLD_SINCE -> searchEstate.soldDate = value
        }
        forceRefresh()
    }

    fun setSearch(value: Boolean, field: Int) {
        when (field) {
            EstateSearch.SCHOOL -> searchEstate.nearbySchool = value
            EstateSearch.SHOP -> searchEstate.nearbyShop = value
            EstateSearch.PARK -> searchEstate.nearbyPark = value
            EstateSearch.SOLD -> searchEstate.sold = value
        }
    }

    fun setSearchNull(field: Int) {
        when (field) {
            EstateSearch.SCHOOL -> searchEstate.nearbySchool = false
            EstateSearch.SHOP -> searchEstate.nearbyShop = false
            EstateSearch.PARK -> searchEstate.nearbyPark = false
            EstateSearch.CITY -> searchEstate.city = null
            EstateSearch.ZIPCODE -> searchEstate.zipCode = null
            EstateSearch.COUNTRY -> searchEstate.country = null
            EstateSearch.TYPE -> searchEstate.type = null
            EstateSearch.PRICE_MIN -> searchEstate.priceMinDollar = null
            EstateSearch.PRICE_MAX -> searchEstate.priceMaxDollar = null
            EstateSearch.AREA_MIN -> searchEstate.areaMin = null
            EstateSearch.AREA_MAX -> searchEstate.areaMax = null
            EstateSearch.ROOMS -> searchEstate.nbRooms = null
            EstateSearch.BEDROOMS -> searchEstate.nbBedrooms = null
            EstateSearch.BATHROOMS -> searchEstate.nbBathrooms = null
            EstateSearch.AGENT -> searchEstate.agentId = null
            EstateSearch.IMAGES -> searchEstate.nbImages = null
            EstateSearch.IN_SALE_SINCE -> searchEstate.entryDate = null
            EstateSearch.SOLD_SINCE -> searchEstate.soldDate = null
            EstateSearch.ALL -> {
                searchEstate = EstateSearch()
                forceRefresh()
            }
        }
    }

    fun forceRefresh() {
        searchEstateLiveData.postValue(searchEstate)
    }

    fun invertCurrency() {
        _isSearchInDollar.value = !_isSearchInDollar.value!!
    }

}