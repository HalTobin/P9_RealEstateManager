package com.openclassrooms.realestatemanager.di

import androidx.room.Database
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.api.GetCoordinatesDeserializer
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDatabase
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.data.repository.CoordinatesRepositoryImpl
import com.openclassrooms.realestatemanager.ui.fragment.ListFragment
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.ImageRepositoryImpl
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.provider.EstateContentProvider
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import com.openclassrooms.realestatemanager.ui.fragment.DetailsFragment
import com.openclassrooms.realestatemanager.ui.fragment.MapFragment
import com.openclassrooms.realestatemanager.viewModel.AddEditEstateViewModel
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    private const val POSITION_STACK_BASE_URL = "http://api.positionstack.com/v1/"

    val applicationModule: Module = module {

        single {
            provideEstateRepository(get())
        }

        single {
            provideImageRepository(get())
        }

        single {
            provideCoordinatesRepository(get())
        }

        single {
            Retrofit
                .Builder()
                .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()).build())
                .baseUrl(POSITION_STACK_BASE_URL)
                .addConverterFactory(createGsonConverter())
                .build()
        }

        single {
            Room.databaseBuilder(androidApplication(), EstateDatabase::class.java, EstateDatabase.DATABASE_NAME)
                .build()
        }

        single {
            provideEstateDao(database = get())
        }

        single {
            provideImageDao(database = get())
        }

        fragment {
            provideListFragment()
        }

        fragment {
            provideMapFragment()
        }

        fragment {
            provideDetailsFragment()
        }

        viewModel {
            provideMainViewModel(get())
        }

        viewModel {
            provideAddEditEstateViewModel(estateRepository = get(), imageRepository = get(), coordinatesRepository = get())
        }

    }

    private fun provideEstateDao(database: EstateDatabase): EstateDao = database.estateDao

    private fun provideImageDao(database: EstateDatabase): ImageDao = database.imageDao

    private fun provideEstateRepository(estateDao: EstateDao): EstateRepository =
        EstateRepositoryImpl(estateDao)

    private fun provideImageRepository(imageDao: ImageDao): ImageRepository =
        ImageRepositoryImpl(imageDao)

    private fun provideCoordinatesRepository(retrofit: Retrofit): CoordinatesRepository =
        CoordinatesRepositoryImpl(retrofit)

    // This allow the use of a custom deserializer (GetCoordinatesDeserializer)
    private fun createGsonConverter(): Converter.Factory {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(object : TypeToken<Coordinates>() {}.type, GetCoordinatesDeserializer())
        val gson: Gson = gsonBuilder.create()
        return GsonConverterFactory.create(gson)
    }

    private fun provideMainViewModel(estateRepository: EstateRepository): MainViewModel =
        MainViewModel(estateRepository)

    private fun provideAddEditEstateViewModel(
        estateRepository: EstateRepository,
        imageRepository: ImageRepository,
        coordinatesRepository: CoordinatesRepository
    ): AddEditEstateViewModel = AddEditEstateViewModel(estateRepository, imageRepository, coordinatesRepository)

    private fun provideListFragment(): ListFragment = ListFragment()

    private fun provideMapFragment(): MapFragment = MapFragment()

    private fun provideDetailsFragment(): DetailsFragment = DetailsFragment()

}