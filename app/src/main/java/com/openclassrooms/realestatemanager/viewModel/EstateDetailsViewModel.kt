package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class EstateDetailsViewModel(private val estateRepository: EstateRepository): ViewModel(), KoinComponent {

    private var estateId: Long? = null

    private val _estate = MutableLiveData<Estate>()
    val estate = _estate

    fun setEstateId(estateId: Long) {
        this.estateId = estateId
        viewModelScope.launch {
            estateRepository.getEstate(estateId).collect {
                _estate.postValue(it)
            }
        }

    }

}