package com.openclassrooms.realestatemanager.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.util.JavaFusedLocationProviderClient
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val estateRepository: EstateRepository by inject()

    private val coordinates: MutableLiveData<Coordinates> by lazy { MutableLiveData<Coordinates>() }

    fun getEstates(): LiveData<List<Estate>> {
        return estateRepository.getEstates().asLiveData()
    }

    fun setLocation(xNewCoordinate: Double, yNewCoordinate: Double) {
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

}