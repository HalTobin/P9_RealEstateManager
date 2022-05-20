package com.openclassrooms.realestatemanager.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.ImageWithDescription

@Database(
    entities = [Estate::class, ImageWithDescription::class],
    version = 1
)
abstract class EstateDatabase: RoomDatabase() {

    abstract val estateDao: EstateDao
    abstract val imageDao: ImageDao

    companion object {

        const val DATABASE_NAME = "estate_db"

    }

}