package com.openclassrooms.realestatemanager.model

import android.content.ContentValues
import android.content.Context
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.util.Utils.fullAddress
import com.openclassrooms.realestatemanager.util.Utils.isValid
import java.util.*

@Entity
data class Estate(
    @PrimaryKey val id: Int? = null,
    var title: String? = null,
    var type: Int? = null,
    var address: String? = null,
    var city: String? = "",
    var zipCode: String? = "",
    var country: String? = "",
    var xCoordinate: Double? = null,
    var yCoordinate: Double? = null,
    var priceDollar: Int? = null,
    var area: Int? = null,
    var nbRooms: Int? = null,
    var nbBathrooms: Int? = null,
    var nbBedrooms: Int? = null,
    var nearbySchool: Boolean? = false,
    var nearbyShop: Boolean? = false,
    var nearbyPark: Boolean? = false,
    var sold: Boolean? = false,
    var entryDate: Long? = null,
    var soldDate: Long? = null,
    var agentId: Int? = null,
    var description: String? = null
) {

    fun getFullAddress(): String {
        return fullAddress(address!!, zipCode, city!!, country!!)
    }

    fun getAddressInPresentation(): String {
        return address.plus("\n").plus(zipCode).plus(" ").plus(city).plus("\n").plus(country)
    }

    companion object {

        const val TYPE_ALL = 0
        const val TYPE_APPARTMENT = 1
        const val TYPE_HOUSE = 2
        const val TYPE_LOFT = 3
        const val TYPE_MANOR = 4
        const val TYPE_TOWNHOUSE = 5

        const val UNCOMPLETED: Int = 10
        const val CANT_FIND_LOCATION: Int = 11

        fun isFilled(
            title: String?,
            address: String?,
            city: String?,
            country: String?,
            coordinates: Coordinates?,
            priceDollar: Int?,
            area: Int?,
            nbRooms: Int?,
            nbBathrooms: Int?,
            nbBedrooms: Int?,
            agentId: Int?,
            description: String?
        ): Boolean {
            var isFilled = true
            if (title != null) {
                if (title.isEmpty()) isFilled = false
            } else isFilled = false
            if (address != null) {
                if (address.isEmpty()) isFilled = false
            } else isFilled = false
            if (city != null) {
                if (city.isEmpty()) isFilled = false
            } else isFilled = false
            if (country != null) {
                if (country.isEmpty()) isFilled = false
            } else isFilled = false
            if (coordinates == null) isFilled = false
            if (priceDollar == null) isFilled = false
            if (area == null) isFilled = false
            if (nbRooms == null) isFilled = false
            if (nbBathrooms == null) isFilled = false
            if (nbBedrooms == null) isFilled = false
            if (agentId == null) isFilled = false
            if (description != null) {
                if (description.isEmpty()) isFilled = false
            } else isFilled = false
            return isFilled
        }

        fun fromContentValues(values: ContentValues): Estate {
            return Estate(
                id = values.getAsInteger("id"),
                type = values.getAsInteger("type"),
                title = values.getAsString("title"),
                address = values.getAsString("address"),
                city = values.getAsString("city"),
                zipCode = values.getAsString("zipCode"),
                country = values.getAsString("country"),
                xCoordinate = values.getAsDouble("xCoordinate"),
                yCoordinate = values.getAsDouble("yCoordinate"),
                priceDollar = values.getAsInteger("priceDollar"),
                area = values.getAsInteger("area"),
                nbRooms = values.getAsInteger("nbRooms"),
                nbBathrooms = values.getAsInteger("nbBathrooms"),
                nbBedrooms = values.getAsInteger("nbBedrooms"),
                nearbySchool = values.getAsBoolean("nearbySchool"),
                nearbyShop = values.getAsBoolean("nearbyShop"),
                nearbyPark = values.getAsBoolean("nearbyPark"),
                sold = values.getAsBoolean("sold"),
                entryDate = values.getAsLong("entryDate"),
                soldDate = values.getAsLong("soldDate"),
                agentId = values.getAsInteger("agent"),
                description = values.getAsString("description")
            )
        }

        fun getEstateTypes(context: Context): List<String> {
            return listOf(
                context.getString(R.string.estate_type_appartment),
                context.getString(R.string.estate_type_house),
                context.getString(R.string.estate_type_loft),
                context.getString(R.string.estate_type_manor),
                context.getString(R.string.estate_type_townhouse)
            )
        }

        fun getEstateTypeFromInt(context: Context, index: Int): String {
            return getEstateTypes(context).get(index)
        }

        val fake_list = listOf(
            Estate(
                id = 1,
                title = "Maison Bagnolet",
                type = TYPE_APPARTMENT,
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
                nearbyPark = false
            ),
            Estate(
                id = 2,
                title = "Appartement Gambetta",
                type = TYPE_APPARTMENT,
                address = "40 avenue Gambetta",
                city = "Paris",
                zipCode = "75020",
                country = "France",
                xCoordinate = 48.86484,
                yCoordinate = 2.397598,
                priceDollar = 700000,
                area = 35,
                nbRooms = 4,
                nbBathrooms = 1,
                nbBedrooms = 2,
                nearbyShop = true,
                nearbySchool = true,
                nearbyPark = true
            ),
            Estate(
                id = 3,
                title = "Studio Nation",
                type = TYPE_APPARTMENT,
                address = "11 rue des Immeubles Industriels",
                city = "Paris",
                zipCode = "75011",
                country = "France",
                xCoordinate = 48.849752,
                yCoordinate = 2.392566,
                priceDollar = 450000,
                area = 20,
                nbRooms = 1,
                nbBathrooms = 1,
                nbBedrooms = 0,
                nearbyShop = false,
                nearbySchool = true,
                nearbyPark = false
            ),
            Estate(
                id = 4,
                title = "Appartement Villette",
                type = TYPE_APPARTMENT,
                address = "10 rue Delesseux",
                city = "Paris",
                zipCode = "75019",
                country = "France",
                xCoordinate = 48.888535,
                yCoordinate = 2.38737,
                priceDollar = 750000,
                area = 45,
                nbRooms = 5,
                nbBathrooms = 1,
                nbBedrooms = 3,
                nearbyShop = true,
                nearbySchool = false,
                nearbyPark = true
            ),
            Estate(
                id = 5,
                title = "Studio Barbès",
                type = TYPE_APPARTMENT,
                address = "13 rue de Sofia",
                city = "Paris",
                zipCode = "75018",
                country = "France",
                xCoordinate = 48.884719,
                yCoordinate = 2.348279,
                priceDollar = 250000,
                area = 20,
                nbRooms = 1,
                nbBathrooms = 1,
                nbBedrooms = 0,
                nearbyShop = false,
                nearbySchool = true,
                nearbyPark = true
            ),
        )

    }

}