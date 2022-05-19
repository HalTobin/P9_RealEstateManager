package com.openclassrooms.realestatemanager.model

import androidx.room.Embedded
import androidx.room.Relation

data class EstateWithImages (
    @Embedded
    val estate: Estate,
    @Relation(
        parentColumn = "id",
        entityColumn = "estate"
    )
    val images: List<ImageWithDescription>
)