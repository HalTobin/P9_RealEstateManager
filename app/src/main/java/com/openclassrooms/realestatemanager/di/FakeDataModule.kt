package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.data.data_source.AgentDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.EstateDatabase
import com.openclassrooms.realestatemanager.data.InMemoryEstateDatabase
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.data.repository.AgentRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.CoordinatesRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.ImageRepositoryImpl
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

// Module dedicated to the app's data sources while running tests
object FakeDataModule {

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
            Retrofit.Builder()
                .baseUrl("http://localhost:4007/")
                .client(
                    OkHttpClient.Builder()
                        .connectTimeout(1, TimeUnit.SECONDS)
                        .readTimeout(1, TimeUnit.SECONDS)
                        .writeTimeout(1, TimeUnit.SECONDS)
                        .build()
                )
                .addConverterFactory(DataModule.createGsonConverter())
                .build()
        }

        single {
            InMemoryEstateDatabase.getInMemoryDatabase(androidApplication())
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

}