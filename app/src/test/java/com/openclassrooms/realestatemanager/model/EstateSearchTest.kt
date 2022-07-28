package com.openclassrooms.realestatemanager.model

import android.util.Log
import com.google.common.truth.Truth
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class EstateSearchTest {

    private val baseQuery =
        "SELECT *,(SELECT COUNT(*) FROM imagewithdescription WHERE imagewithdescription.estateId = Estate.id) AS images FROM Estate "

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() = clearAllMocks()

    @Test
    fun `check search when the estate's type is given`() {
        // Given
        val estateSearch = EstateSearch(type = Estate.TYPE_APARTMENT)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND type = ?"))
    }

    @Test
    fun `check search when the estate's country is given`() {
        // Given
        val estateSearch = EstateSearch(country = "France")

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND country = ?"))
    }

    @Test
    fun `check search when the estate's city is given`() {
        // Given
        val estateSearch = EstateSearch(city = "Paris")

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND city = ?"))
    }

    @Test
    fun `check search when the estate's zip code is given`() {
        // Given
        val estateSearch = EstateSearch(zipCode = "75011")

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND zipCode = ?"))
    }

    @Test
    fun `check search when the estate's minimum price is given`() {
        // Given
        val estateSearch = EstateSearch(priceMinDollar = 500000)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND priceDollar >= ?"))
    }

    @Test
    fun `check search when the estate's maximum price is given`() {
        // Given
        val estateSearch = EstateSearch(priceMaxDollar = 1000000)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND priceDollar <= ?"))
    }

    @Test
    fun `check search when the estate's minimum area is given`() {
        // Given
        val estateSearch = EstateSearch(areaMin = 20)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND area >= ?"))
    }

    @Test
    fun `check search when the estate's maximum area is given`() {
        // Given
        val estateSearch = EstateSearch(areaMax = 45)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND area <= ?"))
    }

    @Test
    fun `check search when the estate's number of room is given`() {
        // Given
        val estateSearch = EstateSearch(nbRooms = 3)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND nbRooms >= ?"))
    }

    @Test
    fun `check search when the estate's number of bedroom is given`() {
        // Given
        val estateSearch = EstateSearch(nbBedrooms = 2)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND nbBedrooms >= ?"))
    }

    @Test
    fun `check search when the estate's number of bathroom type is given`() {
        // Given
        val estateSearch = EstateSearch(nbBathrooms = 2)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND nbBathrooms >= ?"))
    }

    @Test
    fun `check search when looking for an estate near a park`() {
        // Given
        val estateSearch = EstateSearch(nearbyPark = true)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND nearbyPark = ?"))
    }

    @Test
    fun `check search when looking for an estate near a school`() {
        // Given
        val estateSearch = EstateSearch(nearbySchool = true)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND nearbySchool = ?"))
    }

    @Test
    fun `check search when looking for an estate near a shop`() {
        // Given
        val estateSearch = EstateSearch(nearbyShop = true)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND nearbyShop = ?"))
    }

    @Test
    fun `check search when the agent in charge of the estate is given`() {
        // Given
        val estateSearch = EstateSearch(agentId = 1)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND agentId = ?"))
    }

    @Test
    fun `check search when estate's date is given`() {
        // Given
        val estateSearch = EstateSearch(entryDate = 1657843200000)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND entryDate >= ?"))
    }

    @Test
    fun `check search when estate's sold date is given`() {
        // Given
        val estateSearch = EstateSearch(soldDate = 1657843200000)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND soldDate >= ?"))
    }

    @Test
    fun `check search when the estate's minimum number of image is given`() {
        // Given
        val estateSearch = EstateSearch(nbImages = 2)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString())
            .isEqualTo(baseQuery.plus("WHERE sold = ? AND images >= ?"))
    }
}