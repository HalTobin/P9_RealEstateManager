package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val estateRepository: EstateRepository by inject()
    private val _estates: MutableLiveData<List<Estate>> by lazy(LazyThreadSafetyMode.NONE, initializer = {
        MutableLiveData<List<Estate>>()
    })

    fun getEstates(): LiveData<List<Estate>> {
        return estateRepository.getEstates().asLiveData()
    }

}