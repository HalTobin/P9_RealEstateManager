package com.openclassrooms.realestatemanager.model

import com.openclassrooms.realestatemanager.util.Utils.fullAddress
import java.util.*

data class Estate(val id: Long? = null,
                  var title: String,
                  var address: String,
                  var city: String,
                  var zipCode: String? = "",
                  var country: String,
                  var xCoordinate: Double? = null,
                  var yCoordinate: Double? = null,
                  var priceDollar: Int? = null,
                  var area: Int? = null,
                  var nbRooms: Int? = null,
                  var nbBathrooms: Int? = null,
                  var nbBedrooms: Int? = null,
                  var pictures: List<ImageWithDescription>? = listOf(),
                  var nearbySchool:Boolean? = false,
                  var nearbyShop:Boolean? = false,
                  var nearbyPark:Boolean? = false,
                  var status: Int? = AVAILABLE,
                  var entryDate: Long? = null,
                  var soldDate: Long? = null,
                  var agent: String = "",
                  var description: String = "") {

    fun getFullAddress(): String? {
        return fullAddress(address!!, zipCode, city!!, country!!)
    }

    companion object {

        const val AVAILABLE: Int = 0
        const val STAND_BY: Int = 1
        const val SOLD: Int = 2

        fun isFilled(title: String?, address: String?, city: String?, country: String?, coordinates: Coordinates?, priceDollar: Int?, area: Int?, nbRooms: Int?, nbBathrooms: Int?, nbBedrooms: Int?, soldDate: Long?, agent: String?, description: String?): Boolean {
            var uncorrectField = true
            if(title != null) { if(title.isEmpty()) uncorrectField = false } else uncorrectField = false
            if(address != null) { if(address.isEmpty()) uncorrectField = false } else uncorrectField = false
            if(city != null) { if(city.isEmpty()) uncorrectField = false } else uncorrectField = false
            if(country != null) { if(country.isEmpty()) uncorrectField = false } else uncorrectField = false
            if(coordinates == null) uncorrectField = false
            if(priceDollar == null) uncorrectField = false
            if(area == null) uncorrectField = false
            if(nbRooms == null) uncorrectField = false
            if(nbBathrooms == null) uncorrectField = false
            if(nbBedrooms == null) uncorrectField = false
            if(soldDate == null) uncorrectField = false
            if(agent != null) { if(agent.isEmpty()) uncorrectField = false } else uncorrectField = false
            if(description != null) { if(description.isEmpty()) uncorrectField = false } else uncorrectField = false
            return uncorrectField
        }

        val fake_list = listOf(
            Estate(id = 1, title = "Maison Bagnolet", address = "2bis rue de Bagnolet", city = "Bagnolet", zipCode = "93170", country = "France", xCoordinate = 48.868627, yCoordinate = 2.421451,priceDollar = 450000, area = 25, nbRooms = 3, nbBathrooms = 1, nbBedrooms = 1),
            Estate(id = 2, title = "Appartement Gambetta", address = "40 avenue Gambetta", city = "Paris", zipCode = "75020", country = "France", xCoordinate = 48.86484, yCoordinate = 2.397598, priceDollar = 700000, area = 35, nbRooms = 4, nbBathrooms = 1, nbBedrooms = 2),
            Estate(id = 3, title = "Studio Nation", address = "11 rue des Immeubles Industriels", city = "Paris", zipCode = "75011", country = "France", xCoordinate = 48.849752, yCoordinate = 2.392566, priceDollar = 450000, area = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0),
            Estate(id = 4, title = "Appartement Villette", address = "10 rue Delesseux", city = "Paris", zipCode = "75019", country = "France", xCoordinate = 48.888535, yCoordinate = 2.38737, priceDollar = 750000, area = 45, nbRooms = 5, nbBathrooms = 1, nbBedrooms = 3),
            Estate(id = 5, title = "Studio Barbès", address = "13 rue de Sofia", city = "Paris", zipCode = "75018", country = "France", xCoordinate = 48.884719, yCoordinate = 2.348279, priceDollar = 250000, area = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0),
        )
    }

}