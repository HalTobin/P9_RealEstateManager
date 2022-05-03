package com.openclassrooms.realestatemanager.model

data class Estate(val id: Int,
                  var title: String,
                  var neighbor: String,
                  var priceDollar: Int,
                  var surface: Int,
                  var nbRooms: Int,
                  var nbBathrooms: Int,
                  var nbBedrooms: Int,
                  var picturesLink: List<String>? = null) {

    companion object {
        val fake_list = listOf(
            Estate(1, "Maison Bagnolet", "Charonne", 450000, 25, 3, 1, 1),
            Estate(2, "Appartement Gambetta", "Gambetta", 700000, 35, 4, 1, 2),
            Estate(3, "Studio Nation", "Nation", 300000, 20, 1, 1, 0),
            Estate(4, "Appartement Villette", "La Villette", 750000, 45, 5, 1, 3),
            Estate(5, "Studio Barbès", "Barbès", 250000, 20, 1, 1, 0),
        )
    }

}