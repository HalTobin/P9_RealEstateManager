package com.openclassrooms.realestatemanager.data.data_source

import androidx.room.*
import com.openclassrooms.realestatemanager.model.Agent
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {

    @Query("SELECT * FROM agent")
    fun getAgents(): Flow<List<Agent>>

    @Query("SELECT * FROM agent WHERE id = :id")
    fun getAgentById(id: Int): Flow<Agent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: Agent): Long

    @Delete
    suspend fun deleteAgent(agent: Agent)

}