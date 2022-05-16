package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class EstateDetailsViewModel(private val estateRepository: EstateRepository): ViewModel(), KoinComponent {

    private val _estate = MutableLiveData<Estate>()
    val estate = _estate

    init {
        //TODO - pass the id of an Estate
        viewModelScope.launch {
            //TODO - write function to get Estate by id
            //estateRepository.getEstate(id)
        }
    }

}