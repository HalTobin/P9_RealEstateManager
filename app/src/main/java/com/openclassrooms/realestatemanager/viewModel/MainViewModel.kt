package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val estateRepository: EstateRepository by inject()
    private val _estates: MutableLiveData<List<Estate>> by lazy(LazyThreadSafetyMode.NONE, initializer = {
        MutableLiveData<List<Estate>>()
    })

    /*fun getEstates() {
         getEstatesJob?.cancel()
         getEstatesJob = estateRepository.getEstates().onEach {
             estates -> _estates.value =
         }.launchIn(viewModelScope)
    }*/

    fun getEstates(): LiveData<List<Estate>> {
        return estateRepository.getEstates().asLiveData()
    }

}