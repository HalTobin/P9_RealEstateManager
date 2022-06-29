package com.openclassrooms.realestatemanager.model

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.util.Utils.isValid

data class EstateSearch(
    var type: Int? = null,
    var city: String? = "",
    var zipCode: String? = "",
    var country: String? = "",
    var xCoordinate: Double? = null,
    var yCoordinate: Double? = null,
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

    fun getRequest(): SimpleSQLiteQuery {
        var queryString = BASE_QUERY
        val args = arrayListOf<Any>()
        var conditions = false

        if (type != null) {
            queryString += " WHERE type = :$type"
            args.add(type!!)
            conditions = true
        }

        if (country != null)
            if (country!!.isValid()) {
                queryString += " WHERE country = :$country"
                args.add(country!!)
                conditions = true
            }

        if (city != null)
            if (city!!.isValid()) {
                queryString += " WHERE city = :$city"
                args.add(city!!)
                conditions = true
            }

        if (zipCode != null)
            if (zipCode!!.isValid()) {
                queryString += if (conditions) " AND " else " WHERE "; conditions = true
                queryString += "zipCode = :$zipCode"
                args.add(zipCode!!)
            }

        if (priceMinDollar != null) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "priceDollar >= :${priceMinDollar}"
            args.add(priceMinDollar!!)
        }

        if (priceMaxDollar != null) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "priceDollar <= :${priceMaxDollar}"
            args.add(priceMaxDollar!!)
        }

        if (areaMin != null) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "area >= :$areaMin"
            args.add(areaMin!!)
        }

        if (areaMax != null) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "area <= :$areaMax"
            args.add(areaMax!!)
        }

        if (nbRooms != null) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nbRooms >= :$nbRooms"
            args.add(nbRooms!!)
        }

        if (nbBedrooms != null) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nbBedroom >= :$nbBedrooms"
            args.add(nbBedrooms!!)
        }

        if (nbBathrooms != null) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nbBathrooms >= :$nbBathrooms"
            args.add(nbBathrooms!!)
        }

        if (nearbyPark != false) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nearbyPark = :$nearbyPark"
            args.add(nearbyPark!!)
        }

        if (nearbySchool != false) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nearbySchool = :$nearbySchool"
            args.add(nearbySchool!!)
        }

        if (nearbyShop != false) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "nearbyShop = :$nearbyShop"
            args.add(nearbyShop!!)
        }

        if (sold != null){
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "sold = :$sold"
            args.add(sold!!)
        }

        if (agentId != null) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "agentId = :$agentId"
            args.add(agentId!!)
        }

        if (soldDate != null) {
            queryString += if (conditions) " AND " else " WHERE "; conditions = true
            queryString += "soldDate >= ?"
            args.add(soldDate!!)
        }

        if (entryDate != null) {
            queryString += if (conditions) " AND " else " WHERE "
            queryString += "entryDate >= ?"
            args.add(entryDate!!)
        }

        if (nbImages != null) {
            queryString += if (conditions) " AND " else " WHERE "
            queryString += "images >= ?"
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