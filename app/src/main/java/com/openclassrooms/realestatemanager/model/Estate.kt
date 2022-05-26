package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.openclassrooms.realestatemanager.util.Utils.fullAddress
import java.util.*

@Entity
data class Estate(@PrimaryKey val id: Int? = null,
                  var title: String,
                  var address: String,
                  var city: String,
                  var zipCode: String = "",
                  var country: String,
                  var xCoordinate: Double? = null,
                  var yCoordinate: Double? = null,
                  var priceDollar: Int? = null,
                  var area: Int? = null,
                  var nbRooms: Int? = null,
                  var nbBathrooms: Int? = null,
                  var nbBedrooms: Int? = null,
                  var nearbySchool:Boolean? = false,
                  var nearbyShop:Boolean? = false,
                  var nearbyPark:Boolean? = false,
                  var sold: Boolean = false,
                  var entryDate: Long? = null,
                  var soldDate: Long? = null,
                  var agent: String = "",
                  var description: String = "") {

    fun getFullAddress(): String? {
        return fullAddress(address!!, zipCode, city!!, country!!)
    }

    fun getAddressInPresentation(): String {
        return address.plus("\n").plus(zipCode).plus(" ").plus(city).plus("\n").plus(country)
    }

    companion object {

        const val UNCOMPLETED: Int = 10
        const val CANT_FIND_LOCATION: Int = 11

        fun isFilled(title: String?, address: String?, city: String?, country: String?, coordinates: Coordinates?, priceDollar: Int?, area: Int?, nbRooms: Int?, nbBathrooms: Int?, nbBedrooms: Int?, agent: String?, description: String?): Boolean {
            var isFilled = true
            if(title != null) { if(title.isEmpty()) isFilled = false } else isFilled = false
            if(address != null) { if(address.isEmpty()) isFilled = false } else isFilled = false
            if(city != null) { if(city.isEmpty()) isFilled = false } else isFilled = false
            if(country != null) { if(country.isEmpty()) isFilled = false } else isFilled = false
            if(coordinates == null) isFilled = false
            if(priceDollar == null) isFilled = false
            if(area == null) isFilled = false
            if(nbRooms == null) isFilled = false
            if(nbBathrooms == null) isFilled = false
            if(nbBedrooms == null) isFilled = false
            if(agent != null) { if(agent.isEmpty()) isFilled = false } else isFilled = false
            if(description != null) { if(description.isEmpty()) isFilled = false } else isFilled = false
            return isFilled
        }

        val fake_list = listOf(
            Estate(id = 1, title = "Maison Bagnolet", address = "2bis rue de Bagnolet", city = "Bagnolet", zipCode = "93170", country = "France", xCoordinate = 48.868627, yCoordinate = 2.421451,priceDollar = 450000, area = 25, nbRooms = 3, nbBathrooms = 1, nbBedrooms = 1, nearbyShop = false, nearbySchool = false, nearbyPark = false),
            Estate(id = 2, title = "Appartement Gambetta", address = "40 avenue Gambetta", city = "Paris", zipCode = "75020", country = "France", xCoordinate = 48.86484, yCoordinate = 2.397598, priceDollar = 700000, area = 35, nbRooms = 4, nbBathrooms = 1, nbBedrooms = 2, nearbyShop = true, nearbySchool = true, nearbyPark = true),
            Estate(id = 3, title = "Studio Nation", address = "11 rue des Immeubles Industriels", city = "Paris", zipCode = "75011", country = "France", xCoordinate = 48.849752, yCoordinate = 2.392566, priceDollar = 450000, area = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0, nearbyShop = false, nearbySchool = true, nearbyPark = false),
            Estate(id = 4, title = "Appartement Villette", address = "10 rue Delesseux", city = "Paris", zipCode = "75019", country = "France", xCoordinate = 48.888535, yCoordinate = 2.38737, priceDollar = 750000, area = 45, nbRooms = 5, nbBathrooms = 1, nbBedrooms = 3, nearbyShop = true, nearbySchool = false, nearbyPark = true),
            Estate(id = 5, title = "Studio Barbès", address = "13 rue de Sofia", city = "Paris", zipCode = "75018", country = "France", xCoordinate = 48.884719, yCoordinate = 2.348279, priceDollar = 250000, area = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0, nearbyShop = false, nearbySchool = true, nearbyPark = true),
        )
    }

}