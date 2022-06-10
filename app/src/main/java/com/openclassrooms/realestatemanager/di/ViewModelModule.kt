package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import com.openclassrooms.realestatemanager.viewModel.AddEditEstateViewModel
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelModule {

    val viewModelModule: Module = module {

        viewModel {
            provideMainViewModel(get())
        }

        viewModel {
            provideAddEditEstateViewModel(estateRepository = get(), imageRepository = get(), coordinatesRepository = get())
        }

    }

    private fun provideMainViewModel(estateRepository: EstateRepository): MainViewModel =
        MainViewModel(estateRepository)

    private fun provideAddEditEstateViewModel(
        estateRepository: EstateRepository,
        imageRepository: ImageRepository,
        coordinatesRepository: CoordinatesRepository
    ): AddEditEstateViewModel = AddEditEstateViewModel(estateRepository, imageRepository, coordinatesRepository)



}