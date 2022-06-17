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
data class Estate(@PrimaryKey val id: Int? = null,
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
                  var nearbySchool:Boolean? = false,
                  var nearbyShop:Boolean? = false,
                  var nearbyPark:Boolean? = false,
                  var sold: Boolean? = false,
                  var entryDate: Long? = null,
                  var soldDate: Long? = null,
                  var agent: String? = null,
                  var description: String? = null) {

    fun getFullAddress(): String {
        return fullAddress(address!!, zipCode, city!!, country!!)
    }

    fun getAddressInPresentation(): String {
        return address.plus("\n").plus(zipCode).plus(" ").plus(city).plus("\n").plus(country)
    }

    fun getRequest(): SimpleSQLiteQuery {
        var queryString = BASE_QUERY
        val args = arrayListOf<Any>()
        var conditions = false

        if(type != null){
            queryString += " WHERE type = :$type"
            args.add(type!!)
            conditions = true
        }

        if(country!!.isValid()){
            queryString += " WHERE type = :$country"
            args.add(country!!)
            conditions = true
        }

        if(city!!.isValid()){
            queryString += " WHERE type = :$city"
            args.add(city!!)
            conditions = true
        }

        if(zipCode!!.isValid()){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "zipCode = :$zipCode"
            args.add(zipCode!!)
        }

        /*if(sold != null){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "sold = :$sold"
            args.add(sold!!)
        }*/

        if (priceDollar != null){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "priceDollar <= :${priceDollar}"
            args.add(priceDollar!!)
        }

        if (area != null){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "area >= :$area"
            args.add(area!!)
        }

        if (nbRooms != null){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nbRooms >= :$nbRooms"
            args.add(nbRooms!!)
        }

        if (nbBedrooms != null){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nbBedroom >= :$nbBedrooms"
            args.add(nbBedrooms!!)
        }

        if (nbBathrooms != null){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nbBathrooms >= :$nbBathrooms"
            args.add(nbBathrooms!!)
        }

        if (nearbyPark != false){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nearbyPark = :$nearbyPark"
            args.add(nearbyPark!!)
        }

        if (nearbySchool != false){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nearbySchool = :$nearbySchool"
            args.add(nearbySchool!!)
        }

        if (nearbyShop != false){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nearbyShop = :$nearbyShop"
            args.add(nearbyShop!!)
        }

        if (soldDate != null){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "soldDate >= ?"
            args.add(soldDate!!)
        }

        if (entryDate != null){
            queryString += if (conditions) " AND " else " WHERE "
            queryString += "entryDate >= ?"
            args.add(entryDate!!)
        }

        println("REQUEST : $queryString")
        return SimpleSQLiteQuery(queryString, args.toArray())
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

        const val TYPE = 100
        const val CITY = 101
        const val ZIPCODE = 102
        const val COUNTRY = 103
        const val PRICE = 104
        const val AREA = 105
        const val ROOMS = 106
        const val BEDROOMS = 107
        const val BATHROOMS = 108
        const val SCHOOL = 109
        const val SHOP = 110
        const val PARK = 111
        const val AGENT = 112

        const val BASE_QUERY = "SELECT * FROM estate"

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
                agent = values.getAsString("agent"),
                description = values.getAsString("description"))
        }

        fun getEstateTypes(context: Context): List<String> {
            return listOf(context.getString(R.string.estate_type_appartment),
                context.getString(R.string.estate_type_house),
                context.getString(R.string.estate_type_loft),
                context.getString(R.string.estate_type_manor),
                context.getString(R.string.estate_type_townhouse))
        }

        fun getEstateTypeFromInt(context: Context, index: Int): String {
            return getEstateTypes(context).get(index)
        }

        val fake_list = listOf(
            Estate(id = 1, title = "Maison Bagnolet", type = TYPE_APPARTMENT, address = "2bis rue de Bagnolet", city = "Bagnolet", zipCode = "93170", country = "France", xCoordinate = 48.868627, yCoordinate = 2.421451,priceDollar = 450000, area = 25, nbRooms = 3, nbBathrooms = 1, nbBedrooms = 1, nearbyShop = false, nearbySchool = false, nearbyPark = false),
            Estate(id = 2, title = "Appartement Gambetta", type = TYPE_APPARTMENT, address = "40 avenue Gambetta", city = "Paris", zipCode = "75020", country = "France", xCoordinate = 48.86484, yCoordinate = 2.397598, priceDollar = 700000, area = 35, nbRooms = 4, nbBathrooms = 1, nbBedrooms = 2, nearbyShop = true, nearbySchool = true, nearbyPark = true),
            Estate(id = 3, title = "Studio Nation", type = TYPE_APPARTMENT, address = "11 rue des Immeubles Industriels", city = "Paris", zipCode = "75011", country = "France", xCoordinate = 48.849752, yCoordinate = 2.392566, priceDollar = 450000, area = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0, nearbyShop = false, nearbySchool = true, nearbyPark = false),
            Estate(id = 4, title = "Appartement Villette", type = TYPE_APPARTMENT, address = "10 rue Delesseux", city = "Paris", zipCode = "75019", country = "France", xCoordinate = 48.888535, yCoordinate = 2.38737, priceDollar = 750000, area = 45, nbRooms = 5, nbBathrooms = 1, nbBedrooms = 3, nearbyShop = true, nearbySchool = false, nearbyPark = true),
            Estate(id = 5, title = "Studio Barbès", type = TYPE_APPARTMENT, address = "13 rue de Sofia", city = "Paris", zipCode = "75018", country = "France", xCoordinate = 48.884719, yCoordinate = 2.348279, priceDollar = 250000, area = 20, nbRooms = 1, nbBathrooms = 1, nbBedrooms = 0, nearbyShop = false, nearbySchool = true, nearbyPark = true),
        )

    }

}