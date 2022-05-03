package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.flow.Flow

class EstateRepositoryImpl: EstateRepository {
    override fun getEstates(): Flow<List<Estate>> {
        TODO("Not yet implemented")
    }
}