package com.openclassrooms.realestatemanager.data.data_source

import androidx.room.*
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM imagewithdescription")
    fun getAllImages(): Flow<List<ImageWithDescription>>

    @Query("SELECT * FROM imagewithdescription WHERE estateId = :estateId")
    fun getImageByEstateId(estateId: Int): Flow<List<ImageWithDescription>>

    @Query("SELECT * FROM imagewithdescription WHERE id = :id")
    fun getImageById(id: Int): Flow<ImageWithDescription>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageWithDescription)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageWithDescription>)

    @Delete
    suspend fun deleteImage(image: ImageWithDescription)

    @Delete
    suspend fun deletesImages(images: List<ImageWithDescription>)

}