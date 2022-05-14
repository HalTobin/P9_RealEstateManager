package com.openclassrooms.realestatemanager.model

data class Coordinates(
    var xCoordinate: Double,
    var yCoordinate: Double
) {

    fun isUndefined(): Boolean {
        return (xCoordinate == null || yCoordinate == null)
    }

}