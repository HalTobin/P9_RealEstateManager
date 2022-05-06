package com.openclassrooms.realestatemanager.model

import java.util.*

data class Estate(val id: Int,
                  var title: String,
                  var neighbor: String,
                  var address: String,
                  var xCoordinate: Double? = null,
                  var yCoordinate: Double? = null,
                  var priceDollar: Int,
                  var surface: Int,
                  var nbRooms: Int,
                  var nbBathrooms: Int,
                  var nbBedrooms: Int,
                  var picturesLink: List<String>? = null,
                  var nearbySchools:Boolean? = false,
                  var nearbyShops:Boolean? = false,
                  var nearbyParks:Boolean? = false,
                  var status: Int? = 0,
                  var entryDate: Date? = null,
                  var soldDate: Date? = null,
                  var agent: String? = null) {

    companion object {

        const val AVAILABLE: Int = 0
        const val STAND_BY: Int = 1
        const val SOLD: Int = 2

        val fake_list = listOf(
            Estate(id = 1, title = "Maison Bagnolet", neighbor = "Bagnolet", address = "2bis rue de Bagnolet, 93170 Bagnolet, France", priceDollar = 450000, surface = 25, nbRooms = 3, nbBathrooms = 1, nbBedrooms = 1),
            Estate(id = 2, title = "Appartement Gambetta", neighbor = "Gambetta", address = "40 avenue Gambetta, 75020 Paris, France", priceDollar = 700000, surface = 35, nbRooms = 4, nbBathrooms = 1, nbBedrooms = 2),
            Estate(id = 3, title = "Studio Nation", neighbor = "Nation", address = "11 rue des Immeubles Industriels, 75011 Paris, France", priceDollar = 450000, surface = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0),
            Estate(id = 4, title = "Appartement Villette", neighbor = "La Villette", address = "10 rue Delesseux, 75019 Paris, France", priceDollar = 750000, surface = 45, nbRooms = 5, nbBathrooms = 1, nbBedrooms = 3),
            Estate(id = 5, title = "Studio Barbès", neighbor = "Barbès", address = "13 rue de Sofia, 75018 Paris, France", priceDollar = 250000, surface = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0),
        )
    }

}