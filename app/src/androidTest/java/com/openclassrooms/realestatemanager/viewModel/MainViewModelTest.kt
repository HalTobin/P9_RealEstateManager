package com.openclassrooms.realestatemanager.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.data.EstateDatabase
import com.openclassrooms.realestatemanager.data.InMemoryEstateDatabase
import com.openclassrooms.realestatemanager.data.LiveDataUtil.getOrAwaitValue
import com.openclassrooms.realestatemanager.data.data_source.AgentDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.data.repository.AgentRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.ImageRepositoryImpl
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateSearch
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MainViewModelTest {

    private lateinit var context: Context

    private lateinit var mainViewModel: MainViewModel

    private lateinit var agentDao: AgentDao
    private lateinit var estateDao: EstateDao
    private lateinit var imageDao: ImageDao
    private lateinit var database: EstateDatabase

    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
        database = InMemoryEstateDatabase.getInMemoryDatabase(context)
        agentDao = database.agentDao
        estateDao = database.estateDao
        imageDao = database.imageDao

        mainViewModel = MainViewModel(
            estateRepository = EstateRepositoryImpl(estateDao),
            imageRepository = ImageRepositoryImpl(imageDao),
            agentRepository = AgentRepositoryImpl(agentDao)
        )

        estateDao.insert(InMemoryEstateDatabase.estates[0])
        estateDao.insert(InMemoryEstateDatabase.estates[1])

        mainViewModel.viewModelScope.launch {
            imageDao.insertImages(InMemoryEstateDatabase.images1)
            imageDao.insertImages(InMemoryEstateDatabase.images2)
        }
    }

    @After
    @Throws(IOException::class)
    fun clear() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testGetEstates() {
        mainViewModel.viewModelScope.launch {
            assertEquals(mainViewModel.estates.getOrAwaitValue().size, 2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testGetAgents() {
        mainViewModel.viewModelScope.launch {
            assertEquals(mainViewModel.getListOfAgent().getOrAwaitValue().size, 7)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testGetImages() {
        mainViewModel.viewModelScope.launch {
            assertEquals(mainViewModel.getListOfAgent().getOrAwaitValue().size, 6)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testSetLocation() {
        val xCoordinate = 46.048336
        val yCoordinate = 14.504161
        mainViewModel.setLocation(xCoordinate, yCoordinate)
        mainViewModel.viewModelScope.launch {
            assertEquals(
                mainViewModel.coordinates.getOrAwaitValue(),
                Coordinates(xCoordinate, yCoordinate)
            )
        }
    }

    @Test
    @Throws(Exception::class)
    fun testSelectEstate() {
        val selection = InMemoryEstateDatabase.estates[1].id!!
        mainViewModel.selectEstate(selection)
        mainViewModel.viewModelScope.launch {
            assertEquals(mainViewModel.selection.getOrAwaitValue(), selection)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testSetEstateId() {
        val selection = InMemoryEstateDatabase.estatesUi[1]
        mainViewModel.setEstateId(selection.estate.id!!)
        mainViewModel.viewModelScope.launch {
            assertEquals(mainViewModel.estate.getOrAwaitValue(), selection)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateSoldState() {
        val selection = InMemoryEstateDatabase.estates[0]
        mainViewModel.setEstateId(selection.id!!)

        mainViewModel.viewModelScope.launch {
            val expectedTime = mainViewModel.updateSoldState()

            assertTrue(mainViewModel.estate.getOrAwaitValue().estate.sold!!)
            assertEquals(mainViewModel.estate.getOrAwaitValue().estate.soldDate, expectedTime)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testSetSearch() {
        val expected = EstateSearch(
            type = Estate.TYPE_APPARTMENT,
            city = "Paris",
            zipCode = "75020",
            country = "France",
            priceMinDollar = 150000,
            priceMaxDollar = 1250000,
            areaMin = 20,
            areaMax = 75,
            nbRooms = 2,
            nbBedrooms = 1,
            nbBathrooms = 1,
            nearbyPark = false,
            nearbySchool = false,
            nearbyShop = true,
            sold = false,
            entryDate = 1654041600000,
            soldDate = 0L,
            agentId = 1,
            nbImages = 3
        )
        mainViewModel.setSearch(expected.type!!, EstateSearch.TYPE)
        assertEquals(mainViewModel.searchEstate.type, expected.type)

        mainViewModel.setSearch(expected.city!!, EstateSearch.CITY)
        assertEquals(mainViewModel.searchEstate.city, expected.city)

        mainViewModel.setSearch(expected.zipCode!!, EstateSearch.ZIPCODE)
        assertEquals(mainViewModel.searchEstate.zipCode, expected.zipCode)

        mainViewModel.setSearch(expected.country!!, EstateSearch.COUNTRY)
        assertEquals(mainViewModel.searchEstate.country, expected.country)

        mainViewModel.setSearch(expected.priceMinDollar!!, EstateSearch.PRICE_MIN)
        assertEquals(mainViewModel.searchEstate.priceMinDollar, expected.priceMinDollar)

        mainViewModel.setSearch(expected.priceMaxDollar!!, EstateSearch.PRICE_MAX)
        assertEquals(mainViewModel.searchEstate.priceMaxDollar, expected.priceMaxDollar)

        mainViewModel.setSearch(expected.nbRooms!!, EstateSearch.ROOMS)
        assertEquals(mainViewModel.searchEstate.nbRooms, expected.nbRooms)

        mainViewModel.setSearch(expected.nbBedrooms!!, EstateSearch.BEDROOMS)
        assertEquals(mainViewModel.searchEstate.nbBedrooms, expected.nbBedrooms)

        mainViewModel.setSearch(expected.nbBathrooms!!, EstateSearch.BATHROOMS)
        assertEquals(mainViewModel.searchEstate.nbBathrooms, expected.nbBathrooms)

        mainViewModel.setSearch(expected.nearbySchool!!, EstateSearch.SCHOOL)
        assertEquals(mainViewModel.searchEstate.nearbySchool, expected.nearbySchool)

        mainViewModel.setSearch(expected.nearbyShop!!, EstateSearch.SHOP)
        assertEquals(mainViewModel.searchEstate.nearbyShop, expected.nearbyShop)

        mainViewModel.setSearch(expected.nearbyPark!!, EstateSearch.PARK)
        assertEquals(mainViewModel.searchEstate.nearbyPark, expected.nearbyPark)

        mainViewModel.setSearch(expected.agentId!!, EstateSearch.AGENT)
        assertEquals(mainViewModel.searchEstate.agentId, expected.agentId)

        mainViewModel.setSearch(expected.entryDate!!, EstateSearch.IN_SALE_SINCE)
        assertEquals(mainViewModel.searchEstate.entryDate, expected.entryDate)

        mainViewModel.setSearch(expected.soldDate!!, EstateSearch.SOLD_SINCE)
        assertEquals(mainViewModel.searchEstate.soldDate, expected.soldDate)

        mainViewModel.setSearch(expected.nbImages!!, EstateSearch.IMAGES)
        assertEquals(mainViewModel.searchEstate.nbImages, expected.nbImages)

        mainViewModel.setSearch(expected.sold!!, EstateSearch.SOLD)
        assertEquals(mainViewModel.searchEstate.sold, expected.sold)
    }



}