package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class ImageRepositoryImpl(private val dao: ImageDao) : ImageRepository {

    override fun getImages(): Flow<List<ImageWithDescription>> {
        return dao.getAllImages()
    }

    override fun getImagesByEstateId(estateId: Int): Flow<List<ImageWithDescription>> {
        return dao.getImageByEstateId(estateId)
    }

    override fun getImage(id: Int): Flow<ImageWithDescription> {
        return dao.getImageById(id)
    }

    override suspend fun addImage(image: ImageWithDescription) {
        dao.insertImage(image)
    }

    override suspend fun addListOfImages(images: List<ImageWithDescription>) {
        dao.insertImages(images)
    }

    override suspend fun deleteImage(image: ImageWithDescription) {
        dao.deleteImage(image)
    }

    override suspend fun deleteListOfImages(images: List<ImageWithDescription>) {
        dao.deletesImages(images)
    }

}