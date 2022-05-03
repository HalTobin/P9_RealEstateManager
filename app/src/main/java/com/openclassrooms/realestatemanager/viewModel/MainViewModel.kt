package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate

class MainViewModel(savedStateHandle: SavedStateHandle): ViewModel() {

    private val instance: MainViewModel? = null
    private val estateLiveData: MutableLiveData<List<Estate>> = MutableLiveData()

    fun getInstance(): MainViewModel? {
        val result: MainViewModel? =
            instance
        if (result != null) {
            return result
        }
        synchronized(RestaurantRepository::class.java) {
            if (com.example.go4lunch.viewModel.RestaurantViewModel.instance == null) {
                com.example.go4lunch.viewModel.RestaurantViewModel.instance =
                    com.example.go4lunch.viewModel.RestaurantViewModel()
            }
            return com.example.go4lunch.viewModel.RestaurantViewModel.instance
        }
    }

    fun getEstates(): LiveData<List<Estate>> {
        estateLiveData.value = Estate.fake_list
        return estateLiveData
    }

}