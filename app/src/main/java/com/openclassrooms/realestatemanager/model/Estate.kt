package com.openclassrooms.realestatemanager.model

data class Estate(val id: Int,
                  var title: String,
                  var neighbor: String,
                  var price: Int,
                  var surface: Int,
                  var nbRooms: Int,
                  var nbBathrooms: Int,
                  var nbBedrooms: Int) {

}