package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.model.ImageWithDescription
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getImages(): Flow<List<ImageWithDescription>>

    fun getImagesByEstateId(estateId: Int): Flow<List<ImageWithDescription>>

    fun getImage(id: Int): Flow<ImageWithDescription>

    suspend fun addImage(image: ImageWithDescription)

    suspend fun addListOfImages(images: List<ImageWithDescription>)

    suspend fun deleteImage(image: ImageWithDescription)

    suspend fun deleteListOfImages(images : List<ImageWithDescription>)

}