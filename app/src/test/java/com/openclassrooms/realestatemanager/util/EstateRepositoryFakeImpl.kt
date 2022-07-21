package com.openclassrooms.realestatemanager.util

import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateUI
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EstateRepositoryFakeImpl(): EstateRepository {

    private val estates: MutableList<Estate> = mutableListOf(
        Estate(
            id = 1,
            title = "Title 1",
            type = Estate.TYPE_APPARTMENT,
            address = "95 Avenue de la République",
            city = "Paris",
            zipCode = "75011",
            country = "France",
            xCoordinate = 48.863845,
            yCoordinate = 2.38283,
            priceDollar = 1750000,
            area = 50,
            nbRooms = 4,
            nbBedrooms = 2,
            nbBathrooms = 1,
            nearbyShop = true,
            nearbySchool = true,
            nearbyPark = false,
            sold = false,
            entryDate = 1656633600,
            soldDate = 0,
            agentId = 1,
            description = "This is a description"
        ),
        Estate(
            id = 2,
            title = "Title 2",
            type = Estate.TYPE_HOUSE,
            address = "Gosposka ulica 6",
            city = "Ljubljana",
            zipCode = "1000",
            country = "Slovénie",
            xCoordinate = 46.048336,
            yCoordinate = 14.504161,
            priceDollar = 2750000,
            area = 75,
            nbRooms = 4,
            nbBedrooms = 3,
            nbBathrooms = 2,
            nearbyShop = true,
            nearbySchool = false,
            nearbyPark = false,
            sold = true,
            entryDate = 1654041600,
            soldDate = 1656633600,
            agentId = 1,
            description = "This is a description"
        ),
    )

    private val estatesUi: MutableList<EstateUI> = mutableListOf(
        EstateUI(
            estate = estates[0],
            images = listOf()
        ),
        EstateUI(
            estate = estates[1],
            images = listOf()
        ),
    )

    private val agents: List<Agent> = listOf(
        Agent(id = 1, firstName = "Morgana", lastName = "De Santis"),
        Agent(id = 2, firstName = "Clara", lastName = "Saavedra"),
        Agent(id = 3, firstName = "Stanislas", lastName = "Meyer"),
        Agent(id = 4, firstName = "Auguste", lastName = "Bouvier"),
        Agent(id = 5, firstName = "Robert", lastName = "Roche"),
        Agent(id = 6, firstName = "Lucy", lastName = "Guillot"),
        Agent(id = 7, firstName = "Joseph", lastName = "Boutin")
    )

    private val images1: List<ImageWithDescription> = listOf(
        ImageWithDescription(id = 1, estateId = 1, description = "Living Room", imageUrl = "nothing.jpg"),
        ImageWithDescription(id = 6, estateId = 1, description = "Bedroom", imageUrl = "nothing.jpg"),
    )

    private val images2: List<ImageWithDescription> = listOf(
        ImageWithDescription(id = 2, estateId = 2, description = "Entry", imageUrl = "nothing.jpg"),
        ImageWithDescription(id = 3, estateId = 2, description = "Kitchen", imageUrl = "nothing.jpg"),
        ImageWithDescription(id = 4, estateId = 2, description = "Bathroom", imageUrl = "nothing.jpg"),
        ImageWithDescription(id = 5, estateId = 2, description = "Living Room", imageUrl = "nothing.jpg"),
    )

    override fun getEstates(): Flow<List<EstateUI>> {
        val estatesUiFlow: Flow<List<EstateUI>> = flow {
            emit(estatesUi)
        }
        return estatesUiFlow
    }

    override fun searchEstates(query: SimpleSQLiteQuery): Flow<List<EstateUI>> {
        val estatesUiFlow: Flow<List<EstateUI>> = flow {
            emit(estatesUi)
        }
        return estatesUiFlow
    }

    override fun getEstate(id: Int): Flow<EstateUI> {
        val estateUiFlow: Flow<EstateUI> = flow {
            estatesUi.forEach { estateUi ->
                if (estateUi.estate.id == id) emit(estateUi)
            }
        }
        return estateUiFlow
    }

    override suspend fun addEstate(estate: Estate): Long {
        estates.add(estate)
        estatesUi.add(EstateUI(estate, listOf()))
        return estates.indexOf(estate).toLong()
    }

    override suspend fun changeSoldState(id: Int, sold: Boolean) {
        estates.forEach { estate ->
            if (estate.id == id) estate.sold = sold
        }
    }

    override suspend fun changeSoldDate(id: Int, soldDate: Long) {
        estates.forEach { estate ->
            if (estate.id == id) estate.soldDate = soldDate
        }
    }

}