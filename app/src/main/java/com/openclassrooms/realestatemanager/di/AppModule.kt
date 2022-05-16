package com.openclassrooms.realestatemanager.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.api.GetCoordinatesDeserializer
import com.openclassrooms.realestatemanager.data.repository.CoordinatesRepositoryImpl
import com.openclassrooms.realestatemanager.ui.fragment.ListFragment
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.ui.fragment.MapFragment
import com.openclassrooms.realestatemanager.viewModel.AddEditEstateViewModel
import com.openclassrooms.realestatemanager.viewModel.EstateDetailsViewModel
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    private const val POSITION_STACK_BASE_URL = "http://api.positionstack.com/v1/"

    val applicationModule : Module = module {

        single {
            provideEstateRepository()
        }

        single {
            provideCoordinatesRepository(get())
        }

        single {
            Retrofit
                .Builder()
                .client(
                    OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor()).build())
                .baseUrl(POSITION_STACK_BASE_URL)
                .addConverterFactory(createGsonConverter())
                .build()
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

        viewModel {
            provideAddEditEstateViewModel(get(), get())
        }

        viewModel {
            provideEstateDetailsViewModel(get())
        }

    }

    private fun provideEstateRepository(): EstateRepository = EstateRepositoryImpl()

    //TODO - Check how to retrieve api key from manifest
    private fun provideCoordinatesRepository(retrofit: Retrofit): CoordinatesRepository = CoordinatesRepositoryImpl(retrofit)

    // This allow the use of a custom deserializer (GetCoordinatesDeserializer)
    private fun createGsonConverter(): Converter.Factory {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(
            object : TypeToken<Coordinates>() {}.type,
            GetCoordinatesDeserializer()
        )
        val gson: Gson = gsonBuilder.create()
        return GsonConverterFactory.create(gson)
    }

    private fun provideMainViewModel(estateRepository: EstateRepository): MainViewModel = MainViewModel(estateRepository)

    private fun provideAddEditEstateViewModel(estateRepository: EstateRepository, coordinatesRepository: CoordinatesRepository): AddEditEstateViewModel = AddEditEstateViewModel(estateRepository, coordinatesRepository)

    private fun provideEstateDetailsViewModel(estateRepository: EstateRepository): EstateDetailsViewModel = EstateDetailsViewModel(estateRepository)

    private fun provideListFragment(): ListFragment = ListFragment()

    private fun provideMapFragment(): MapFragment = MapFragment()

}