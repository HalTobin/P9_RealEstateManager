package com.openclassrooms.realestatemanager.data

import android.content.ContentValues
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.data.data_source.AgentDao
import com.openclassrooms.realestatemanager.data.data_source.EstateDao
import com.openclassrooms.realestatemanager.data.data_source.ImageDao
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.ImageWithDescription

@Database(
    entities = [Estate::class, ImageWithDescription::class, Agent::class],
    version = 1
)
abstract class EstateDatabase : RoomDatabase() {

    abstract val estateDao: EstateDao
    abstract val imageDao: ImageDao
    abstract val agentDao: AgentDao

    companion object {

        const val DATABASE_NAME = "estate_db"

        // Prepopulate the database with a list of agents
        fun prepopulateDatabase(): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val contentValues = ContentValues()
                    contentValues.put("id", 1)
                    contentValues.put("firstName", "Morgana")
                    contentValues.put("lastName", "De Santis")
                    db.insert("Agent", OnConflictStrategy.IGNORE, contentValues)

                    contentValues.put("id", 2)
                    contentValues.put("firstName", "Clara")
                    contentValues.put("lastName", "Saavedra")
                    db.insert("Agent", OnConflictStrategy.IGNORE, contentValues)

                    contentValues.put("id", 3)
                    contentValues.put("firstName", "Stanislas")
                    contentValues.put("lastName", "Meyer")
                    db.insert("Agent", OnConflictStrategy.IGNORE, contentValues)

                    contentValues.put("id", 4)
                    contentValues.put("firstName", "Auguste")
                    contentValues.put("lastName", "Bouvier")
                    db.insert("Agent", OnConflictStrategy.IGNORE, contentValues)

                    contentValues.put("id", 5)
                    contentValues.put("firstName", "Robert")
                    contentValues.put("lastName", "Roche")
                    db.insert("Agent", OnConflictStrategy.IGNORE, contentValues)

                    contentValues.put("id", 6)
                    contentValues.put("firstName", "Lucy")
                    contentValues.put("lastName", "Guillot")
                    db.insert("Agent", OnConflictStrategy.IGNORE, contentValues)

                    contentValues.put("id", 7)
                    contentValues.put("firstName", "Joseph")
                    contentValues.put("lastName", "Boutin")
                    db.insert("Agent", OnConflictStrategy.IGNORE, contentValues)
                }
            }
        }

    }

}