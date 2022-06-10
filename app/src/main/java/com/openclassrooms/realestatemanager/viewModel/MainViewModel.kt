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
import kotlinx.coroutines.launch

class MainViewModel(private val estateRepository: EstateRepository) : ViewModel() {

    private var estateId: Int? = null

    private val _estate = MutableLiveData<EstateWithImages>()
    val estate = _estate

    private val _estates = MutableLiveData<List<Estate>>()
    val estates = _estates

    private val _coordinates = MutableLiveData<Coordinates>()
    val coordinates = _coordinates

    private val _selection = MutableLiveData<Int>()
    val selection = _selection

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
            estateRepository.changeSoldState(_estate.value!!.estate.id!!, !_estate.value!!.estate.sold)
            estateRepository.changeSoldDate(_estate.value!!.estate.id!!, System.currentTimeMillis())
        }
    }

}