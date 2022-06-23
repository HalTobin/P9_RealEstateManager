package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.model.Agent
import kotlinx.coroutines.flow.Flow

interface AgentRepository {

    fun getAgents(): Flow<List<Agent>>

    fun getAgent(id: Int): Flow<Agent>

    suspend fun addEstate(agent: Agent): Long

}