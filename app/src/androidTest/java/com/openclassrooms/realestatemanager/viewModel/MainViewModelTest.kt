package com.openclassrooms.realestatemanager.viewModel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.data.EstateDatabase
import com.openclassrooms.realestatemanager.data.InMemoryEstateDatabase
import com.openclassrooms.realestatemanager.util.MainCoroutineRule
import com.openclassrooms.realestatemanager.data.data_source.AgentDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.data.getOrAwaitValue
import com.openclassrooms.realestatemanager.data.repository.AgentRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.ImageRepositoryImpl
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateSearch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MainViewModelTest {

    private lateinit var context: Context

    private lateinit var mainViewModel: MainViewModel

    private lateinit var agentDao: AgentDao
    private lateinit var estateDao: EstateDao
    private lateinit var imageDao: ImageDao
    private lateinit var database: EstateDatabase

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

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
        mainViewModel.estates.postValue(InMemoryEstateDatabase.estatesUi)
    }

    @After
    @Throws(IOException::class)
    fun clear() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_get_estates() = runTest {
        advanceUntilIdle()
        assertEquals(2, mainViewModel.estates.getOrAwaitValue().size)
    }

    @Test
    @Throws(Exception::class)
    fun test_set_location() = runTest {
        val xCoordinate = 46.048336
        val yCoordinate = 14.504161
        mainViewModel.setLocation(xCoordinate, yCoordinate)
        advanceUntilIdle()
        assertEquals(
            Coordinates(xCoordinate, yCoordinate),
            mainViewModel.coordinates.getOrAwaitValue()
        )
    }

    @Test
    @Throws(Exception::class)
    fun test_select_estate() = runTest {
        val selection = InMemoryEstateDatabase.estates[1].id!!
        mainViewModel.selectEstate(selection)
        advanceUntilIdle()
        assertEquals(selection, mainViewModel.selection.getOrAwaitValue())
    }

    @Test
    @Throws(Exception::class)
    fun test_set_estate_id() = runTest {
        val selection = InMemoryEstateDatabase.estatesUi[1]
        mainViewModel.setEstateId(selection.estate.id!!)
        advanceUntilIdle()
        assertEquals(selection, mainViewModel.estate.getOrAwaitValue())
    }

    @Test
    @Throws(Exception::class)
    fun test_update_sold_state() = runTest {
        val selection = InMemoryEstateDatabase.estates[0]
        mainViewModel.setEstateId(selection.id!!)

        val expectedTime = mainViewModel.updateSoldState()
        advanceUntilIdle()

        val res = mainViewModel.estate.getOrAwaitValue()
        assertTrue(res.estate.sold!!)
        assertEquals(expectedTime, res.estate.soldDate)
    }

    @Test
    @Throws(Exception::class)
    fun test_set_search() = runTest {
        val expected = EstateSearch(
            type = Estate.TYPE_APARTMENT,
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
        assertEquals(expected.type, mainViewModel.searchEstate.type)

        mainViewModel.setSearch(expected.city!!, EstateSearch.CITY)
        assertEquals(expected.city, mainViewModel.searchEstate.city)

        mainViewModel.setSearch(expected.zipCode!!, EstateSearch.ZIPCODE)
        assertEquals(expected.zipCode, mainViewModel.searchEstate.zipCode)

        mainViewModel.setSearch(expected.country!!, EstateSearch.COUNTRY)
        assertEquals(expected.country, mainViewModel.searchEstate.country)

        mainViewModel.setSearch(expected.priceMinDollar!!, EstateSearch.PRICE_MIN)
        assertEquals(expected.priceMinDollar, mainViewModel.searchEstate.priceMinDollar)

        mainViewModel.setSearch(expected.priceMaxDollar!!, EstateSearch.PRICE_MAX)
        assertEquals(expected.priceMaxDollar, mainViewModel.searchEstate.priceMaxDollar)

        mainViewModel.setSearch(expected.nbRooms!!, EstateSearch.ROOMS)
        assertEquals(expected.nbRooms, mainViewModel.searchEstate.nbRooms)

        mainViewModel.setSearch(expected.nbBedrooms!!, EstateSearch.BEDROOMS)
        assertEquals(expected.nbBedrooms, mainViewModel.searchEstate.nbBedrooms)

        mainViewModel.setSearch(expected.nbBathrooms!!, EstateSearch.BATHROOMS)
        assertEquals(expected.nbBathrooms, mainViewModel.searchEstate.nbBathrooms)

        mainViewModel.setSearch(expected.nearbySchool!!, EstateSearch.SCHOOL)
        assertEquals(expected.nearbySchool, mainViewModel.searchEstate.nearbySchool)

        mainViewModel.setSearch(expected.nearbyShop!!, EstateSearch.SHOP)
        assertEquals(expected.nearbyShop, mainViewModel.searchEstate.nearbyShop)

        mainViewModel.setSearch(expected.nearbyPark!!, EstateSearch.PARK)
        assertEquals(expected.nearbyPark, mainViewModel.searchEstate.nearbyPark)

        mainViewModel.setSearch(expected.agentId!!, EstateSearch.AGENT)
        assertEquals(expected.agentId, mainViewModel.searchEstate.agentId)

        mainViewModel.setSearch(expected.entryDate!!, EstateSearch.IN_SALE_SINCE)
        assertEquals(expected.entryDate, mainViewModel.searchEstate.entryDate)

        mainViewModel.setSearch(expected.soldDate!!, EstateSearch.SOLD_SINCE)
        assertEquals(expected.soldDate, mainViewModel.searchEstate.soldDate)

        mainViewModel.setSearch(expected.nbImages!!, EstateSearch.IMAGES)
        assertEquals(expected.nbImages, mainViewModel.searchEstate.nbImages)

        mainViewModel.setSearch(expected.sold!!, EstateSearch.SOLD)
        assertEquals(expected.sold, mainViewModel.searchEstate.sold)
    }

    @Test
    @Throws(Exception::class)
    fun test_set_search_to_null() = runTest {
        test_set_search()

        mainViewModel.setSearchNull(EstateSearch.ALL)

        assertEquals(null, mainViewModel.searchEstate.type)
        assertEquals("", mainViewModel.searchEstate.city)
        assertEquals("", mainViewModel.searchEstate.zipCode)
        assertEquals("", mainViewModel.searchEstate.country)
        assertEquals(null, mainViewModel.searchEstate.priceMinDollar)
        assertEquals(null, mainViewModel.searchEstate.priceMaxDollar)
        assertEquals(null, mainViewModel.searchEstate.nbRooms)
        assertEquals(null, mainViewModel.searchEstate.nbBedrooms)
        assertEquals(null, mainViewModel.searchEstate.nbBathrooms)
        assertEquals(false, mainViewModel.searchEstate.nearbySchool)
        assertEquals(false, mainViewModel.searchEstate.nearbyShop)
        assertEquals(false, mainViewModel.searchEstate.nearbyPark)
        assertEquals(null, mainViewModel.searchEstate.agentId)
        assertEquals(null, mainViewModel.searchEstate.entryDate)
        assertEquals(null, mainViewModel.searchEstate.soldDate)
        assertEquals(null, mainViewModel.searchEstate.nbImages)
        assertEquals(false, mainViewModel.searchEstate.sold)
    }
}