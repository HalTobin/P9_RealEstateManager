package com.openclassrooms.realestatemanager.model

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery

data class EstateSearch(
    var type: Int? = null,
    var city: String? = "",
    var zipCode: String? = "",
    var country: String? = "",
    var priceMinDollar: Int? = null,
    var priceMaxDollar: Int? = null,
    var areaMin: Int? = null,
    var areaMax: Int? = null,
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
    var nbImages: Int? = null
) {

    // Generate an sql query to search estates that matches the given parameters
    fun getRequest(): SimpleSQLiteQuery {
        var queryString = BASE_QUERY
        val args = arrayListOf<Any>()

        if (sold != null) {
            queryString += " WHERE sold = ?"
            args.add(sold!!)
        }

        if (type != null) {
            queryString += " AND type = ?"
            args.add(type!!)
        }

        if (country != null)
            if (country!!.isNotEmpty()) {
                queryString += " AND country = ?"
                args.add(country!!)
            }

        if (city != null)
            if (city!!.isNotEmpty()) {
                queryString += " AND city = ?"
                args.add(city!!)
            }

        if (zipCode != null)
            if (zipCode!!.isNotEmpty()) {
                queryString += " AND zipCode = ?"
                args.add(zipCode!!)
            }

        if (priceMinDollar != null) {
            queryString += " AND priceDollar >= ?"
            args.add(priceMinDollar!!)
        }

        if (priceMaxDollar != null) {
            queryString += " AND priceDollar <= ?"
            args.add(priceMaxDollar!!)
        }

        if (areaMin != null) {
            queryString += " AND area >= ?"
            args.add(areaMin!!)
        }

        if (areaMax != null) {
            queryString += " AND area <= ?"
            args.add(areaMax!!)
        }

        if (nbRooms != null) {
            queryString += " AND nbRooms >= ?"
            args.add(nbRooms!!)
        }

        if (nbBedrooms != null) {
            queryString += " AND nbBedrooms >= ?"
            args.add(nbBedrooms!!)
        }

        if (nbBathrooms != null) {
            queryString += " AND nbBathrooms >= ?"
            args.add(nbBathrooms!!)
        }

        if (nearbyPark != false) {
            queryString += " AND nearbyPark = ?"
            args.add(nearbyPark!!)
        }

        if (nearbySchool != false) {
            queryString += " AND nearbySchool = ?"
            args.add(nearbySchool!!)
        }

        if (nearbyShop != false) {
            queryString += " AND nearbyShop = ?"
            args.add(nearbyShop!!)
        }

        if (agentId != null) {
            queryString += " AND agentId = ?"
            args.add(agentId!!)
        }

        if (soldDate != null) {
            queryString += " AND soldDate >= ?"
            args.add(soldDate!!)
        }

        if (entryDate != null) {
            queryString += " AND entryDate >= ?"
            args.add(entryDate!!)
        }

        if (nbImages != null) {
            queryString += " AND images >= ?"
            args.add(nbImages!!)
        }

        Log.i("SEARCH ESTATE", "REQUEST : $queryString")
        return SimpleSQLiteQuery(queryString, args.toArray())
    }

    companion object {

        const val TYPE = 100
        const val CITY = 101
        const val ZIPCODE = 102
        const val COUNTRY = 103
        const val PRICE_MIN = 104
        const val PRICE_MAX = 105
        const val AREA_MIN = 106
        const val AREA_MAX = 107
        const val ROOMS = 108
        const val BEDROOMS = 109
        const val BATHROOMS = 110
        const val SCHOOL = 111
        const val SHOP = 112
        const val PARK = 113
        const val AGENT = 114
        const val IN_SALE_SINCE = 115
        const val SOLD_SINCE = 116
        const val IMAGES = 117
        const val SOLD = 118
        const val ALL = 120

        const val BASE_QUERY =
            "SELECT *,(SELECT COUNT(*) FROM imagewithdescription WHERE imagewithdescription.estateId = Estate.id) AS images FROM Estate"

    }

}