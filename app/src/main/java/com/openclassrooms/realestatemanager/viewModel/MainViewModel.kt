package com.openclassrooms.realestatemanager.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateWithImages
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.util.JavaFusedLocationProviderClient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val estateRepository: EstateRepository) : ViewModel() {

    private var estateId: Int? = null

    private val _estate = MutableLiveData<EstateWithImages>()
    val estate = _estate

    private val _estates = MutableLiveData<List<EstateWithImages>>()
    val estates = _estates

    private val _coordinates = MutableLiveData<Coordinates>()
    val coordinates = _coordinates

    private val _selection = MutableLiveData<Int>()
    val selection = _selection

    private val _closeDetails = MutableLiveData(false)
    val closeDetails = _closeDetails

    private val _isSearchInDollar = MutableLiveData(true)
    val isSearchInDollar = _isSearchInDollar

    private val _searchEstate = MutableLiveData(Estate())
    val searchEstate = _searchEstate

    var query = "SELECT * FROM estate"

    fun getEstates(): LiveData<List<EstateWithImages>> {
        return estateRepository.getEstates().asLiveData()
    }

    private fun setLocation(xNewCoordinate: Double, yNewCoordinate: Double) {
        _coordinates.postValue(Coordinates(xNewCoordinate, yNewCoordinate))
    }

    @SuppressLint("MissingPermission")
    fun findCurrentLocation(context: Context?) {
        val myLocationClient = JavaFusedLocationProviderClient(context)
        myLocationClient.currentLocation.addOnSuccessListener { location: Location? ->
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
            estateRepository.changeSoldState(_estate.value!!.estate.id!!, !_estate.value!!.estate.sold!!)
            estateRepository.changeSoldDate(_estate.value!!.estate.id!!, System.currentTimeMillis())
        }
    }

    fun closeDetails() {
        _closeDetails.postValue(true)
        //_closeDetails.postValue(false)
    }

    fun setSearch(value: String, field: Int) {
        when(field) {
            Estate.CITY -> _searchEstate.value?.city = value
            Estate.ZIPCODE -> _searchEstate.value?.zipCode = value
            Estate.COUNTRY -> _searchEstate.value?.country = value
            Estate.AGENT -> _searchEstate.value?.agent = value
        }
    }

    fun setSearch(value: Int, field: Int) {
        when(field) {
            Estate.TYPE -> _searchEstate.value?.type = value
            Estate.PRICE -> _searchEstate.value?.priceDollar = value
            Estate.AREA -> _searchEstate.value?.area = value
            Estate.ROOMS -> _searchEstate.value?.nbRooms = value
            Estate.BEDROOMS -> _searchEstate.value?.nbBedrooms = value
            Estate.BATHROOMS -> _searchEstate.value?.nbBathrooms = value
        }
    }

    fun setSearch(value: Boolean, field: Int) {
        when(field) {
            Estate.SCHOOL -> _searchEstate.value?.nearbySchool = value
            Estate.SHOP -> _searchEstate.value?.nearbyShop = value
            Estate.PARK -> _searchEstate.value?.nearbyPark = value
        }
    }

    fun invertCurrency() {
        _isSearchInDollar.value = !_isSearchInDollar.value!!
    }

    fun enterSearch() {
        viewModelScope.launch {
            val temp = estateRepository.searchEstates(_searchEstate.value!!.getRequest())
            temp.let { _estates.postValue(it) }
        }
    }

}