package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import com.openclassrooms.realestatemanager.viewModel.AddEditEstateViewModel
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

// Module dedicated to the ViewModels
object ViewModelModule {

    val viewModelModule: Module = module {

        viewModel {
            provideMainViewModel(estateRepository = get(), imageRepository = get(), agentRepository = get())
        }

        viewModel {
            provideAddEditEstateViewModel(estateRepository = get(), imageRepository = get(), coordinatesRepository = get(), agentRepository = get())
        }

    }

    private fun provideMainViewModel(estateRepository: EstateRepository, imageRepository: ImageRepository, agentRepository: AgentRepository): MainViewModel =
        MainViewModel(estateRepository, imageRepository, agentRepository)

    private fun provideAddEditEstateViewModel(
        estateRepository: EstateRepository,
        imageRepository: ImageRepository,
        coordinatesRepository: CoordinatesRepository,
        agentRepository: AgentRepository
    ): AddEditEstateViewModel = AddEditEstateViewModel(estateRepository, imageRepository, coordinatesRepository, agentRepository)

}