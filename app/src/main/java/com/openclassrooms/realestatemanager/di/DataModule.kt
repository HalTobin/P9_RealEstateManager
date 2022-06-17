package com.openclassrooms.realestatemanager.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.api.GetCoordinatesDeserializer
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDatabase
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.data.repository.CoordinatesRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.ImageRepositoryImpl
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {

    private const val POSITION_STACK_BASE_URL = "http://api.positionstack.com/v1/"

    val dataModule: Module = module {
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
                .fallbackToDestructiveMigration()
                .build()
        }

        single {
            provideEstateDao(database = get())
        }

        single {
            provideImageDao(database = get())
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

}