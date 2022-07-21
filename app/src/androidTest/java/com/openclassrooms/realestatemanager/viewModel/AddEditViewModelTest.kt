package com.openclassrooms.realestatemanager.viewModel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.data.EstateDatabase
import com.openclassrooms.realestatemanager.data.InMemoryEstateDatabase
import com.openclassrooms.realestatemanager.data.MainCoroutineRule
import com.openclassrooms.realestatemanager.data.data_source.AgentDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.data.getOrAwaitValue
import com.openclassrooms.realestatemanager.data.repository.AgentRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.CoordinatesRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.EstateRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.ImageRepositoryImpl
import com.openclassrooms.realestatemanager.di.DataModule
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.util.Utils.fromEuroToDollar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class AddEditViewModelTest {

    private val responseFile =
        "src/androidTest/resources/CoordinatesApiPlaceholder.json"

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(DataModule.createGsonConverter())
        .build()

    private lateinit var context: Context

    private lateinit var addEditViewModel: AddEditEstateViewModel

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

        addEditViewModel = AddEditEstateViewModel(
            estateRepository = EstateRepositoryImpl(estateDao),
            imageRepository = ImageRepositoryImpl(imageDao),
            coordinatesRepository = CoordinatesRepositoryImpl(api),
            agentRepository = AgentRepositoryImpl(agentDao)
        )

        estateDao.insert(InMemoryEstateDatabase.estates[0])
        estateDao.insert(InMemoryEstateDatabase.estates[1])

        runBlocking { imageDao.insertImages(InMemoryEstateDatabase.images1) }

    }

    @After
    @Throws(IOException::class)
    fun clear() {
        database.close()
    }

    /*@Test
    @Throws
    fun testSearchLocation() = runTest {
        val expected = Coordinates(48.863845, 2.38283)
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody()
        )

        testSetCountry()
        testSetCity()
        testSetZip()
        testSetAddress()
        advanceUntilIdle()
        addEditViewModel.searchLocation()
        advanceUntilIdle()

        assertEquals(expected, addEditViewModel.coordinates.value)
    }*/

    @Test
    @Throws(Exception::class)
    fun testLoadEstate() = runTest {
        val selection = InMemoryEstateDatabase.estatesUi[0]
        addEditViewModel.loadEstate(selection.estate.id!!)
        advanceUntilIdle()
        assertEquals(selection.estate.id!!, addEditViewModel.currentEstateId)
        assertEquals(selection.estate.title, addEditViewModel.title.getOrAwaitValue())
        assertEquals(selection.estate.type, addEditViewModel.type.getOrAwaitValue())
        assertEquals(selection.estate.country, addEditViewModel.country.getOrAwaitValue())
        assertEquals(selection.estate.city, addEditViewModel.city.getOrAwaitValue())
        assertEquals(selection.estate.zipCode, addEditViewModel.zip.getOrAwaitValue())
        assertEquals(selection.estate.address, addEditViewModel.address.getOrAwaitValue())
        assertEquals(selection.estate.area, addEditViewModel.area.getOrAwaitValue())
        assertEquals(selection.estate.priceDollar, addEditViewModel.priceAsDollar)
        assertTrue(addEditViewModel.isDollar.getOrAwaitValue()!!)
        assertEquals(
            selection.estate.xCoordinate,
            addEditViewModel.coordinates.getOrAwaitValue()!!.xCoordinate
        )
        assertEquals(
            selection.estate.yCoordinate,
            addEditViewModel.coordinates.getOrAwaitValue()!!.yCoordinate
        )
        assertEquals(selection.images, addEditViewModel.pictures.getOrAwaitValue())
        assertEquals(selection.estate.nearbyPark, addEditViewModel.nearbyPark.getOrAwaitValue())
        assertEquals(selection.estate.nearbyShop, addEditViewModel.nearbyShop.getOrAwaitValue())
        assertEquals(selection.estate.nearbySchool, addEditViewModel.nearbySchool.getOrAwaitValue())
        assertEquals(selection.estate.agentId, addEditViewModel.agent.getOrAwaitValue())
        assertEquals(selection.estate.description, addEditViewModel.description.getOrAwaitValue())
        assertEquals(selection.estate.nbRooms, addEditViewModel.nbRooms.getOrAwaitValue())
        assertEquals(selection.estate.nbBathrooms, addEditViewModel.nbBathrooms.getOrAwaitValue())
        assertEquals(selection.estate.nbBedrooms, addEditViewModel.nbBedrooms.getOrAwaitValue())
        assertEquals(selection.estate.entryDate, addEditViewModel.entryDate.getOrAwaitValue())
        assertEquals(selection.estate.sold, addEditViewModel.sold.getOrAwaitValue())
    }

    @Test
    @Throws
    fun testChangeCurrency() = runTest {
        addEditViewModel.changeCurrency()
        advanceUntilIdle()
        assertFalse(addEditViewModel.isDollar.value!!)
    }

    @Test
    @Throws
    fun testSetTitle() = runTest {
        val expected = "Test"
        addEditViewModel.setTitle(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.title.value)
    }

    @Test
    @Throws
    fun testSetType() = runTest {
        val expected = Estate.TYPE_APPARTMENT
        addEditViewModel.setType(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.type.value)
    }

    @Test
    @Throws
    fun testSetCountry() = runTest {
        val expected = "France"
        addEditViewModel.setCountry(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.country.value)
    }

    @Test
    @Throws
    fun testSetCity() = runTest {
        val expected = "Paris"
        addEditViewModel.setCity(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.city.value)
    }

    @Test
    @Throws
    fun testSetZip() = runTest {
        val expected = "75011"
        addEditViewModel.setZip(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.zip.value)
    }

    @Test
    @Throws
    fun testSetAddress() = runTest {
        val expected = "95 Avenue De La République"
        addEditViewModel.setAddress(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.address.value)
    }

    @Test
    @Throws
    fun testSetArea() = runTest {
        val expected = 50
        addEditViewModel.setArea(expected.toString())
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.area.value)
    }

    @Test
    @Throws
    fun testSetPrice() = runTest {
        val expected = 100000
        addEditViewModel.setPrice(expected.toString())
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.price.value)
    }

    @Test
    @Throws
    fun testRefreshPriceAsDollar() = runTest {
        val given = 100
        val expected = given.fromEuroToDollar()
        addEditViewModel.setPrice(given.toString())
        assertEquals(given, addEditViewModel.priceAsDollar)
        addEditViewModel.changeCurrency()
        assertEquals(expected, addEditViewModel.priceAsDollar)
    }

    @Test
    @Throws
    fun testSetRooms() = runTest {
        val expected = 5
        addEditViewModel.setRooms(expected.toString())
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.nbRooms.value)
    }

    @Test
    @Throws
    fun testSetBedrooms() = runTest {
        val expected = 3
        addEditViewModel.setBedrooms(expected.toString())
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.nbBedrooms.value)
    }

    @Test
    @Throws
    fun testSetBathrooms() = runTest {
        val expected = 1
        addEditViewModel.setBathrooms(expected.toString())
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.nbBathrooms.value)
    }

    @Test
    @Throws
    fun testSetPark() = runTest {
        val expected = true
        addEditViewModel.setPark(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.nearbyPark.value)
    }

    @Test
    @Throws
    fun testSetSchool() = runTest {
        val expected = true
        addEditViewModel.setSchool(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.nearbySchool.value)
    }

    @Test
    @Throws
    fun testSetShop() = runTest {
        val expected = true
        addEditViewModel.setShop(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.nearbyShop.value)
    }

    @Test
    @Throws
    fun testSetAgent() = runTest {
        val expected = 1
        addEditViewModel.setAgent(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.agent.value)
    }

    @Test
    @Throws
    fun testSetDescription() = runTest {
        val expected = "This is a description"
        addEditViewModel.setDescription(expected)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.description.value)
    }

    @Test
    @Throws
    fun testAddPictureWhenAddingAnEstate() = runTest {
        val givenFile = "folder/file.jpg"
        val givenText = "Room"
        val expected = listOf(
            ImageWithDescription(
                estateId = -1,
                description = givenText,
                imageUrl = givenFile
            )
        )
        addEditViewModel.addPicture(givenFile, givenText)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.pictures.value)
    }

    @Test
    @Throws
    fun testAddPictureWhenEditingAnEstate() = runTest {
        testLoadEstate()
        val givenFile = "folder/file.jpg"
        val givenText = "Room"
        val expected =
            ImageWithDescription(estateId = 1, description = givenText, imageUrl = givenFile)
        addEditViewModel.addPicture(givenFile, givenText)
        advanceUntilIdle()
        assertTrue(addEditViewModel.pictures.value!!.contains(expected))
    }

    @Test
    @Throws
    fun testRemovePicture() = runTest {
        testLoadEstate()
        val expected = listOf(InMemoryEstateDatabase.images1[0])
        val removed = InMemoryEstateDatabase.images1[1]
        addEditViewModel.removePicture(removed)
        advanceUntilIdle()
        assertEquals(expected, addEditViewModel.pictures.value)
        assertFalse(addEditViewModel.pictures.value!!.contains(removed))
    }

    @Test
    @Throws
    fun testSaveEstate() = runTest {
        val expected = InMemoryEstateDatabase.estatesUi[0]

        testLoadEstate()
        addEditViewModel.saveEstate()
        advanceUntilIdle()

        estateDao.getEstates().take(1).collect { estatesInDatabase ->
            assertTrue(estatesInDatabase.contains(expected))
        }

    }

}