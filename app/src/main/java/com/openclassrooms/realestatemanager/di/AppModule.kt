package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModule {

    val applicationModule : Module = module {

        single {
            provideEstateRepository()
        }

        viewModel {
            provideMainViewModel(get())
        }

        /*viewModel {
            provideMainAndroidViewModel(get<Application>(), get<EstateRepository>())
        }*/

    }

    fun provideEstateRepository(): EstateRepository = EstateRepositoryImpl()

    fun provideMainViewModel(estateRepository: EstateRepository): MainViewModel = MainViewModel()

    //fun provideMainAndroidViewModel(application: Application, estateRepository: EstateRepository): MainAndroidViewModel = MainAndroidViewModel(application)

}