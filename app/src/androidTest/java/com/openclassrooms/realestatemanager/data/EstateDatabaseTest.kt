package com.openclassrooms.realestatemanager.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.data.data_source.AgentDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateUI
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class EstateDatabaseTest {

    private val estates: List<Estate> = mutableListOf(
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

    private val estatesUi: List<EstateUI> = mutableListOf(
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

    private lateinit var agentDao: AgentDao
    private lateinit var estateDao: EstateDao
    private lateinit var imageDao: ImageDao
    private lateinit var database: EstateDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, EstateDatabase::class.java)
            .addCallback(EstateDatabase.prepopulateDatabase())
            .build()
        agentDao = database.agentDao
        estateDao = database.estateDao
        imageDao = database.imageDao

        estateDao.insert(estates[0])
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testWriteAndReadEstate() = runTest {
        estateDao.insert(estates[1])

        estateDao.getEstates().take(1).collect {
            assertTrue(it.contains(estatesUi[1]))
        }
    }

    @Test
    @Throws(Exception::class)
    fun testWriteAndReadEstateById() = runTest {
        estateDao.insert(estates[1])

        estateDao.getEstateById(estates[1].id!!).take(1).collect {
            assertEquals(estates[1].id, it.estate.id)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testChangeSoldState() = runTest {
        estateDao.changeSoldState(estates[0].id!!, true)

        estateDao.getEstateById(estates[0].id!!).take(1).collect {
            assertEquals(true, it.estate.sold)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testChangeSoldDate() = runTest {
        estateDao.changeSoldDate(estates[0].id!!, 1657283786000)

        estateDao.getEstateById(estates[0].id!!).take(1).collect {
            assertEquals(1657283786000, it.estate.soldDate)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testGetAgents() = runTest {
        agentDao.getAgents().take(1).collect {
            assertEquals(agents, it)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testWriteAndReadImage() = runTest {
        imageDao.insertImage(images1[0])

        imageDao.getAllImages().take(1).collect {
            assertTrue(it.contains(images1[0]))
        }
    }

    @Test
    @Throws(Exception::class)
    fun testWriteAndReadImages() = runTest {
        imageDao.insertImages(images1)

        imageDao.getAllImages().take(1).collect {
            assertTrue(it.size == 2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testWriteAndReadImagesByEstateId() = runTest {
        estateDao.insert(estates[1])
        imageDao.insertImages(images1)
        imageDao.insertImages(images2)

        imageDao.getImageByEstateId(estates[0].id!!).take(1).collect {
            it.forEach { image ->
                assertTrue(image.estateId == estates[0].id!!)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteImage() = runTest {
        imageDao.insertImages(images1)
        imageDao.deleteImage(images1[0])

        imageDao.getAllImages().take(1).collect {
            assertTrue(!it.contains(images1[0]))
        }
    }
}