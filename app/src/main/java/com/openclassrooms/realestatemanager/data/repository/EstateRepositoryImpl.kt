package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EstateRepositoryImpl: EstateRepository {

    private val estates = mutableListOf(Estate.fake_list[0], Estate.fake_list[1], Estate.fake_list[2], Estate.fake_list[3], Estate.fake_list[4])

    override fun getEstates(): Flow<List<Estate>> {
        val estatesFlow: Flow<List<Estate>> = flow {
            emit(estates)
        }
        return estatesFlow
    }

    override fun getEstate(id: Long): Flow<Estate> {
        val estateFlow: Flow<Estate> = flow {
            emit(estates[id.toInt()])
        }
        return estateFlow
    }

    override fun addEstate(estate: Estate) {
        estates.add(estate)
    }

}