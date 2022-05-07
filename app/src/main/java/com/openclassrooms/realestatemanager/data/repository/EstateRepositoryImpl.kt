package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EstateRepositoryImpl: EstateRepository {

    override fun getEstates(): Flow<List<Estate>> {
        val estateFlow: Flow<List<Estate>> = flow {
            emit(Estate.fake_list)
        }
        return estateFlow
    }

}