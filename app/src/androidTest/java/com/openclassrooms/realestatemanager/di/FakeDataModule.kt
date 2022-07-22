package com.openclassrooms.realestatemanager.di

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.api.GetCoordinatesDeserializer
import com.openclassrooms.realestatemanager.data.data_source.AgentDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.EstateDatabase
import com.openclassrooms.realestatemanager.data.InMemoryEstateDatabase
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.data.repository.AgentRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.CoordinatesRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.ImageRepositoryImpl
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FakeDataModule {

    private const val POSITION_STACK_BASE_URL = "http://api.positionstack.com/v1/"

    val fakeDataModule: Module = module {
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
            provideAgentRepository(get())
        }

        single {
            Retrofit
                .Builder()
                .client(OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor())
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build())
                .baseUrl(MockWebServer().url("/"))
                .addConverterFactory(createGsonConverter())
                .build()
        }

        single {
            InMemoryEstateDatabase.getInMemoryDatabase(ApplicationProvider.getApplicationContext())
        }

        single {
            provideEstateDao(database = get())
        }

        single {
            provideImageDao(database = get())
        }

        single {
            provideAgentDao(database = get())
        }
    }


    private fun provideEstateDao(database: EstateDatabase): EstateDao = database.estateDao

    private fun provideImageDao(database: EstateDatabase): ImageDao = database.imageDao

    private fun provideAgentDao(database: EstateDatabase): AgentDao = database.agentDao

    private fun provideEstateRepository(estateDao: EstateDao): EstateRepository =
        EstateRepositoryImpl(estateDao)

    private fun provideImageRepository(imageDao: ImageDao): ImageRepository =
        ImageRepositoryImpl(imageDao)

    private fun provideCoordinatesRepository(retrofit: Retrofit): CoordinatesRepository =
        CoordinatesRepositoryImpl(retrofit)

    private fun provideAgentRepository(agentDao: AgentDao): AgentRepository =
        AgentRepositoryImpl(agentDao)

    // This allow the use of a custom deserializer (GetCoordinatesDeserializer)
    fun createGsonConverter(): Converter.Factory {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(object : TypeToken<Coordinates>() {}.type, GetCoordinatesDeserializer())
        val gson: Gson = gsonBuilder.create()
        return GsonConverterFactory.create(gson)
    }

}