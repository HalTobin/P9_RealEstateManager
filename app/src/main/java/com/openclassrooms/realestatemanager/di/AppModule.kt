package com.openclassrooms.realestatemanager.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.api.GetCoordinatesDeserializer
import com.openclassrooms.realestatemanager.api.PositionStackApi
import com.openclassrooms.realestatemanager.api.RetrofitClient
import com.openclassrooms.realestatemanager.data.repository.CoordinatesRepositoryImpl
import com.openclassrooms.realestatemanager.ui.fragment.ListFragment
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.fragment.MapFragment
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object AppModule {

    private const val POSITION_STACK_BASE_URL = "https://api.positionstack.com/v1/"

    val applicationModule : Module = module {

        /*scope<MainActivity> {
            scoped {  }
        }*/

        single {
            provideEstateRepository()
        }

        single {
            provideCoordinatesRepository(get())
        }

        single {
            createGsonConverter()?.let { it1 ->
                Retrofit.Builder().baseUrl(POSITION_STACK_BASE_URL).addConverterFactory(
                    it1
                ).build().create(PositionStackApi::class.java)
            }
        }

        fragment {
            provideListFragment()
        }

        fragment {
            provideMapFragment()
        }

        viewModel {
            provideMainViewModel(get(), get())
        }

    }

    private fun provideEstateRepository(): EstateRepository = EstateRepositoryImpl()

    private fun provideCoordinatesRepository(myApi: PositionStackApi): CoordinatesRepository = CoordinatesRepositoryImpl(myApi)

    // This allow the use of a custom deserializer (GetCoordinatesDeserializer)
    private fun createGsonConverter(): Converter.Factory? {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(
            object : TypeToken<Coordinates>() {}.type,
            GetCoordinatesDeserializer()
        )
        val gson: Gson = gsonBuilder.create()
        return GsonConverterFactory.create(gson)
    }

    private fun provideMainViewModel(estateRepository: EstateRepository, coordinatesRepository: CoordinatesRepository): MainViewModel = MainViewModel(estateRepository, coordinatesRepository)

    private fun provideListFragment(): ListFragment = ListFragment()

    private fun provideMapFragment(): MapFragment = MapFragment()

}