package com.openclassrooms.realestatemanager.model

import com.google.common.truth.Truth
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class EstateSearchTest {

    private val dummyEstate = Estate(
        id = 1,
        title = "Maison Bagnolet",
        type = Estate.TYPE_APPARTMENT,
        address = "2bis rue de Bagnolet",
        city = "Bagnolet",
        zipCode = "93170",
        country = "France",
        xCoordinate = 48.868627,
        yCoordinate = 2.421451,
        priceDollar = 450000,
        area = 25,
        nbRooms = 3,
        nbBathrooms = 1,
        nbBedrooms = 1,
        nearbyShop = false,
        nearbySchool = false,
        nearbyPark = false,
        description = "This is a description"
    )

    private val baseQuery = "SELECT *,(SELECT COUNT(*) FROM imagewithdescription WHERE imagewithdescription.estateId = Estate.id) AS images FROM Estate "

    @After
    fun tearDown() = clearAllMocks()

    @Test
    fun `check search when estate's type is given`() {
        // Given
        val estateSearch = EstateSearch(type = Estate.TYPE_APPARTMENT)

        // When
        val query = estateSearch.getRequest()

        // Then
        Truth.assertThat(query.sql.toString()).isEqualTo(baseQuery.plus("WHERE country = ? AND sold = ?"))
    }

}