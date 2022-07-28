package com.openclassrooms.realestatemanager.model

import com.google.common.truth.Truth
import com.openclassrooms.realestatemanager.model.Estate.Companion.isFilled
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

class EstateTest {

    private val dummyEstate = Estate(
        id = 1,
        title = "Maison Bagnolet",
        type = Estate.TYPE_APARTMENT,
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

    @After
    fun tearDown() = clearAllMocks()

    @Test
    fun `check the functions dedicated to display the estate's address in the estate's details fragment`() {
        // Given
        val estate = dummyEstate
        val expectedAddressInPresentation = "2bis rue de Bagnolet\n93170 Bagnolet\nFrance"

        // When
        val addressInPresentation = estate.getAddressInPresentation()

        // Then
        Truth.assertThat(addressInPresentation).isEqualTo(expectedAddressInPresentation)
    }

    @Test
    fun `check if false is returned when none of the important fields are filled`() {
        // Given
        val title = ""
        val address = ""
        val city = ""
        val country = ""
        val coordinates = null
        val priceDollar = null
        val area = null
        val nbRooms = null
        val nbBathrooms = null
        val nbBedrooms = null
        val agentId = null
        val description = ""

        // When
        val isFilled = isFilled(
            title = title,
            address = address,
            city = city,
            country = country,
            coordinates = coordinates,
            priceDollar = priceDollar,
            area = area,
            nbRooms = nbRooms,
            nbBathrooms = nbBathrooms,
            nbBedrooms = nbBedrooms,
            agentId = agentId,
            description = description
        )

        // Then
        Truth.assertThat(isFilled).isFalse()
    }

    @Test
    fun `check if false is returned when some important fields isn't filled`() {
        // Given
        val title = dummyEstate.title
        val address = dummyEstate.address
        val city = null
        val country = ""
        val coordinates = null
        val priceDollar = dummyEstate.priceDollar
        val area = dummyEstate.area
        val nbRooms = dummyEstate.nbRooms
        val nbBathrooms = dummyEstate.nbBathrooms
        val nbBedrooms = dummyEstate.nbBedrooms
        val agentId = null
        val description = dummyEstate.description

        // When
        val isFilled = isFilled(
            title = title,
            address = address,
            city = city,
            country = country,
            coordinates = coordinates,
            priceDollar = priceDollar,
            area = area,
            nbRooms = nbRooms,
            nbBathrooms = nbBathrooms,
            nbBedrooms = nbBedrooms,
            agentId = agentId,
            description = description
        )

        // Then
        Truth.assertThat(isFilled).isFalse()
    }

    @Test
    fun `check if true is returned when all the important fields are filled`() {
        // Given
        val title = dummyEstate.title
        val address = dummyEstate.address
        val city = dummyEstate.city
        val country = dummyEstate.country
        val coordinates = Coordinates(dummyEstate.xCoordinate!!, dummyEstate.yCoordinate!!)
        val priceDollar = dummyEstate.priceDollar
        val area = dummyEstate.area
        val nbRooms = dummyEstate.nbRooms
        val nbBathrooms = dummyEstate.nbBathrooms
        val nbBedrooms = dummyEstate.nbBedrooms
        val agentId = dummyEstate.agentId
        val description = dummyEstate.description

        // When
        val isFilled = isFilled(
            title = title,
            address = address,
            city = city,
            country = country,
            coordinates = coordinates,
            priceDollar = priceDollar,
            area = area,
            nbRooms = nbRooms,
            nbBathrooms = nbBathrooms,
            nbBedrooms = nbBedrooms,
            agentId = agentId,
            description = description
        )

        // Then
        Truth.assertThat(isFilled).isFalse()
    }

}