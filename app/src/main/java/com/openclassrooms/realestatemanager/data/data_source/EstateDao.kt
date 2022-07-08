package com.openclassrooms.realestatemanager.data.data_source

import android.database.Cursor
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateUI
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate WHERE id = :id")
    fun getEstateWithCursor(id: Int): Cursor

    @Query("SELECT * FROM estate")
    fun getEstates(): Flow<List<EstateUI>>

    @RawQuery
    fun searchEstates(query: SupportSQLiteQuery): Flow<List<EstateUI>>

    @Query("SELECT * FROM estate WHERE id = :id")
    fun getEstateById(id: Int): Flow<EstateUI>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstate(estate: Estate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(estate: Estate): Long

    @Update
    fun updateEstate(estate: Estate?): Int

    @Delete
    suspend fun deleteEstate(estate: Estate)

    @Query("UPDATE estate SET sold = :soldState WHERE id = :id")
    suspend fun changeSoldState(id: Int, soldState: Boolean)

    @Query("UPDATE estate SET soldDate = :soldDate WHERE id = :id")
    suspend fun changeSoldDate(id: Int, soldDate: Long)

}