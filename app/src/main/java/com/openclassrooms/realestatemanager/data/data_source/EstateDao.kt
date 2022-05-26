package com.openclassrooms.realestatemanager.data.data_source

import androidx.room.*
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate")
    fun getEstates(): Flow<List<EstateWithImages>>

    @Query("SELECT * FROM estate WHERE id = :id")
    fun getEstateById(id: Int): Flow<EstateWithImages>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstate(estate: Estate): Long

    @Query("UPDATE estate SET sold = :soldState WHERE id = :id")
    suspend fun changeSoldState(id: Int, soldState: Boolean)

}