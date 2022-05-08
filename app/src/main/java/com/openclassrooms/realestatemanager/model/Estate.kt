package com.openclassrooms.realestatemanager.model

import java.util.*

data class Estate(val id: Long,
                  var title: String,
                  var neighborhood: String,
                  var address: String,
                  var city: String,
                  var zipCode: String? = "",
                  var country: String,
                  var xCoordinate: Double? = null,
                  var yCoordinate: Double? = null,
                  var priceDollar: Int,
                  var surface: Int,
                  var nbRooms: Int,
                  var nbBathrooms: Int,
                  var nbBedrooms: Int,
                  var pictures: List<ImageWithDescription>? = ArrayList(),
                  var nearbySchools:Boolean? = false,
                  var nearbyShops:Boolean? = false,
                  var nearbyParks:Boolean? = false,
                  var status: Int? = 0,
                  var entryDate: Date? = null,
                  var soldDate: Date? = null,
                  var agent: String? = null) {

    fun getFullAddress(): String {
        return address.plus(", ").plus(zipCode).plus(city).plus(", ").plus(country)
    }

    companion object {

        const val AVAILABLE: Int = 0
        const val STAND_BY: Int = 1
        const val SOLD: Int = 2

        val fake_list = listOf(
            Estate(id = 1, title = "Maison Bagnolet", neighborhood = "Bagnolet", address = "2bis rue de Bagnolet", city = "Bagnolet", zipCode = "93170", country = "France", xCoordinate = 48.868627, yCoordinate = 2.421451,priceDollar = 450000, surface = 25, nbRooms = 3, nbBathrooms = 1, nbBedrooms = 1),
            Estate(id = 2, title = "Appartement Gambetta", neighborhood = "Gambetta", address = "40 avenue Gambetta", city = "Paris", zipCode = "75020", country = "France", xCoordinate = 48.86484, yCoordinate = 2.397598, priceDollar = 700000, surface = 35, nbRooms = 4, nbBathrooms = 1, nbBedrooms = 2),
            Estate(id = 3, title = "Studio Nation", neighborhood = "Nation", address = "11 rue des Immeubles Industriels", city = "Paris", zipCode = "75011", country = "France", xCoordinate = 48.849752, yCoordinate = 2.392566, priceDollar = 450000, surface = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0),
            Estate(id = 4, title = "Appartement Villette", neighborhood = "La Villette", address = "10 rue Delesseux", city = "Paris", zipCode = "75019", country = "France", xCoordinate = 48.888535, yCoordinate = 2.38737, priceDollar = 750000, surface = 45, nbRooms = 5, nbBathrooms = 1, nbBedrooms = 3),
            Estate(id = 5, title = "Studio Barbès", neighborhood = "Barbès", address = "13 rue de Sofia", city = "Paris", zipCode = "75018", country = "France", xCoordinate = 48.884719, yCoordinate = 2.348279, priceDollar = 250000, surface = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0),
        )
    }

}