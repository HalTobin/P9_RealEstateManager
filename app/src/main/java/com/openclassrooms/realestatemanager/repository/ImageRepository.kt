package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateWithImages
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getImages(): Flow<List<ImageWithDescription>>

    suspend fun getImage(id: Int): Flow<ImageWithDescription>

    suspend fun addImage(image: ImageWithDescription)

}