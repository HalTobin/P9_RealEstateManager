package com.openclassrooms.realestatemanager.model

data class ImageWithDescription(
    val id: Long,
    val estateId: Long,
    val description: String = "",
    val imageUrl: String
) {
}