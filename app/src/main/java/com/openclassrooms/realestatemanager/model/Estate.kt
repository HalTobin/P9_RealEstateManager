package com.openclassrooms.realestatemanager.model

import android.content.ContentValues
import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.R

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

    fun getAddressInPresentation(): String {
        return address.plus("\n").plus(zipCode).plus(" ").plus(city).plus("\n").plus(country)
    }

    companion object {

        const val TYPE_APARTMENT = 1
        const val TYPE_HOUSE = 2
        //const val TYPE_LOFT = 3
        //const val TYPE_MANOR = 4
        //const val TYPE_TOWNHOUSE = 5

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
                agentId = values.getAsInteger("agentId"),
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
            return getEstateTypes(context)[index]
        }

    }

}