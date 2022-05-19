package com.openclassrooms.realestatemanager.data.data_source

import androidx.room.*
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM imagewithdescription WHERE estateId = :estateId")
    fun getImageByEstateId(estateId: Int): Flow<List<ImageWithDescription>>

    @Query("SELECT * FROM imagewithdescription WHERE id = :id")
    suspend fun getImageById(id: Int): ImageWithDescription?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageWithDescription)

    @Delete
    suspend fun deleteImage(image: ImageWithDescription)

}