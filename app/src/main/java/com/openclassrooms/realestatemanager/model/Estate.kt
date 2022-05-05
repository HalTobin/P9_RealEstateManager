package com.openclassrooms.realestatemanager.model

data class Estate(val id: Int,
                  var title: String,
                  var neighbor: String,
                  //var address: String,
                  var xCoordinate: Double? = null,
                  var yCoordinate: Double? = null,
                  var priceDollar: Int,
                  var surface: Int,
                  var nbRooms: Int,
                  var nbBathrooms: Int,
                  var nbBedrooms: Int,
                  var picturesLink: List<String>? = null) {

    companion object {
        val fake_list = listOf(
            Estate(1, "Maison Bagnolet", "Charonne", 48.863069, 2.422059,450000, 25, 3, 1, 1),
            Estate(2, "Appartement Gambetta", "Gambetta", 48.864864, 2.397704,700000, 35, 4, 1, 2),
            Estate(3, "Studio Nation", "Nation", 48.849617, 2.392734, 300000, 20, 1, 1, 0),
            Estate(4, "Appartement Villette", "La Villette", 48.888561, 2.387255,750000, 45, 5, 1, 3),
            Estate(5, "Studio Barbès", "Barbès", 48.884660, 2.348723, 250000, 20, 1, 1, 0),
        )
    }

}