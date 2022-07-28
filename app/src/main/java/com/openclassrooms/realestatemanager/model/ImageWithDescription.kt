package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Estate::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("estateId"),
    onDelete = ForeignKey.CASCADE)]
)
data class ImageWithDescription(
    @PrimaryKey val id: Int? = null,
    var estateId: Int,
    val description: String = "",
    val imageUrl: String
)