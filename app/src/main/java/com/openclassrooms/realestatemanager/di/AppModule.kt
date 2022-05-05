package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.ui.fragment.ListFragment
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.ui.fragment.MapFragment
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModule {

    val applicationModule : Module = module {

        single {
            provideEstateRepository()
        }

        fragment {
            provideListFragment()
        }

        fragment {
            provideMapFragment()
        }

        viewModel {
            provideMainViewModel(get())
        }

    }

    private fun provideEstateRepository(): EstateRepository = EstateRepositoryImpl()

    private fun provideMainViewModel(estateRepository: EstateRepository): MainViewModel = MainViewModel()

    private fun provideListFragment(): ListFragment = ListFragment()

    private fun provideMapFragment(): MapFragment = MapFragment()

}