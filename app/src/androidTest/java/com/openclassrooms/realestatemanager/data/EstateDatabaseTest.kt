package com.openclassrooms.realestatemanager.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.data.data_source.AgentDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.model.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class EstateDatabaseTest {

    private lateinit var agentDao: AgentDao
    private lateinit var estateDao: EstateDao
    private lateinit var imageDao: ImageDao
    private lateinit var database: EstateDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = InMemoryEstateDatabase.getInMemoryDatabase(context)
        agentDao = database.agentDao
        estateDao = database.estateDao
        imageDao = database.imageDao

        estateDao.insert(InMemoryEstateDatabase.estates[0])
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_write_and_read_estate() = runTest {
        val indexOfExpected = 1
        estateDao.insert(InMemoryEstateDatabase.estates[indexOfExpected])

        estateDao.getEstates().take(1).collect {
            assertTrue(it.contains(InMemoryEstateDatabase.estatesUi[indexOfExpected]))
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_write_and_read_estate_by_id() = runTest {
        val indexOfExpected = 1
        estateDao.insert(InMemoryEstateDatabase.estates[indexOfExpected])

        estateDao.getEstateById(InMemoryEstateDatabase.estates[indexOfExpected].id!!).take(1).collect {
            assertEquals(InMemoryEstateDatabase.estates[indexOfExpected].id, it.estate.id)
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_search() = runTest {
        val search = EstateSearch(type = Estate.TYPE_APARTMENT)
        estateDao.insert(InMemoryEstateDatabase.estates[1])

        estateDao.searchEstates(search.getRequest()).take(1).collect {
            assertEquals(it.size, 1)
            assertEquals(it[0].estate.type, Estate.TYPE_APARTMENT)
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_change_sold_state() = runTest {
        val estateIndex = 0
        estateDao.changeSoldState(InMemoryEstateDatabase.estates[estateIndex].id!!, true)

        estateDao.getEstateById(InMemoryEstateDatabase.estates[estateIndex].id!!).take(1).collect {
            assertEquals(true, it.estate.sold)
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_change_sold_date() = runTest {
        val estateIndex = 0
        estateDao.changeSoldDate(InMemoryEstateDatabase.estates[estateIndex].id!!, 1657283786000)

        estateDao.getEstateById(InMemoryEstateDatabase.estates[estateIndex].id!!).take(1).collect {
            assertEquals(1657283786000, it.estate.soldDate)
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_get_agents() = runTest {
        agentDao.getAgents().take(1).collect {
            assertEquals(InMemoryEstateDatabase.agents, it)
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_write_and_read_image() = runTest {
        val expected = InMemoryEstateDatabase.images1[0]

        imageDao.insertImage(expected)

        imageDao.getAllImages().take(1).collect {
            assertTrue(it.contains(expected))
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_write_and_read_images() = runTest {
        imageDao.insertImages(InMemoryEstateDatabase.images1)

        imageDao.getAllImages().take(1).collect {
            assertEquals(it.size, 2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_write_and_read_images_by_estate_id() = runTest {
        val expected = InMemoryEstateDatabase.estates[0].id!!

        estateDao.insert(InMemoryEstateDatabase.estates[1])
        imageDao.insertImages(InMemoryEstateDatabase.images1)
        imageDao.insertImages(InMemoryEstateDatabase.images2)

        imageDao.getImageByEstateId(expected).take(1).collect {
            it.forEach { image ->
                assertEquals(image.estateId, expected)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_delete_image() = runTest {
        imageDao.insertImages(InMemoryEstateDatabase.images1)
        imageDao.deleteImage(InMemoryEstateDatabase.images1[0])

        imageDao.getAllImages().take(1).collect {
            assertTrue(!it.contains(InMemoryEstateDatabase.images1[0]))
        }
    }
}