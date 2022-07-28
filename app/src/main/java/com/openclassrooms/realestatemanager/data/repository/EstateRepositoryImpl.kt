package com.openclassrooms.realestatemanager.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateUI
import com.openclassrooms.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.flow.Flow

class EstateRepositoryImpl(private val dao: EstateDao) : EstateRepository {

    override fun getEstates(): Flow<List<EstateUI>> {
        return dao.getEstates()
    }

    override fun getEstate(id: Int): Flow<EstateUI> {
        return dao.getEstateById(id)
    }

    override fun searchEstates(query: SimpleSQLiteQuery): Flow<List<EstateUI>> {
        return dao.searchEstates(query)
    }

    override suspend fun addEstate(estate: Estate): Long {
        return dao.insertEstate(estate)
    }

    override suspend fun changeSoldState(id: Int, sold: Boolean) {
        return dao.changeSoldState(id, sold)
    }

    override suspend fun changeSoldDate(id: Int, soldDate: Long) {
        return dao.changeSoldDate(id, soldDate)
    }

}